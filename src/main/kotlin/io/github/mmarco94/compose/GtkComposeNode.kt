package io.github.mmarco94.compose

import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gobject.GObject
import org.gnome.gtk.Widget

internal abstract class GtkComposeNode<out G : GObject?>(
    val gObject: G,
) {
    var modifier: Modifier = Modifier

    fun applyModifier(modifier: Modifier) {
        this.modifier.undo(gObject as Widget)
        this.modifier = modifier
        this.modifier.apply(gObject)
    }

    abstract fun add(index: Int, child: GtkComposeNode<GObject>)
    abstract fun remove(index: Int)
    abstract fun clear()
}

internal open class LeafComposeNode<G : Widget>(gObject: G) : GtkComposeNode<G>(gObject) {
    override fun add(index: Int, child: GtkComposeNode<GObject>) = throw UnsupportedOperationException()
    override fun remove(index: Int) = throw UnsupportedOperationException()
    override fun clear() = throw UnsupportedOperationException()
}

internal open class SingleChildComposeNode<G : Widget>(
    gObject: G,
    val add: G.(GtkComposeNode<GObject>) -> Unit,
    val remove: G.() -> Unit,
) : GtkComposeNode<G>(gObject) {
    private var added = false
    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        require(!added) { "Multiple child added to ${gObject::class.java.simpleName}" }
        require(index == 0)
        added = true
        gObject.add(child)
    }

    override fun remove(index: Int) {
        require(index == 0)
        require(added)
        added = false
        gObject.remove()
    }

    override fun clear() {
        if (added) {
            remove(0)
        }
    }
}

internal class VirtualComposeNode<G : GObject>(
    val nodeCreator: (G) -> GtkComposeNode<G>,
) : GtkComposeNode<Nothing?>(null) {
    private var parentCreator: GtkComposeNode<G>? = null
    private val children = mutableListOf<GtkComposeNode<GObject>>()
    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        children.add(index, child)
        parentCreator?.add(index, child)
    }

    override fun remove(index: Int) {
        children.removeAt(index)
        parentCreator?.remove(index)
    }

    override fun clear() {
        children.clear()
        parentCreator?.clear()
    }

    fun setParent(parent: G?) {
        parentCreator?.clear()
        parentCreator = if (parent != null) {
            nodeCreator(parent).also {
                children.forEachIndexed { index, child -> it.add(index, child) }
            }
        } else {
            null
        }
    }
}

internal class VirtualComposeNodeContainer<G : GObject>(gObject: G) : GtkComposeNode<G>(gObject) {
    private val children = mutableListOf<VirtualComposeNode<G>>()
    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        child as VirtualComposeNode<G>
        children.add(index, child)
        child.setParent(gObject)
    }

    override fun remove(index: Int) {
        children.removeAt(index).setParent(null)
    }

    override fun clear() {
        children.forEach { it.setParent(null) }
        children.clear()
    }
}

internal abstract class GtkContainerComposeNode<G : GObject, C : GObject>(gObject: G) : GtkComposeNode<G>(gObject) {
    private val _children = mutableListOf<C>()
    protected val children: List<C> = _children
    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        val childWidget = child.gObject as C
        _children.add(index, childWidget)
    }

    override fun remove(index: Int) {
        _children.removeAt(index)
    }

    override fun clear() {
        _children.clear()
    }

    companion object {
        fun <G : GObject, C : GObject> appendOnly(
            gObject: G,
            add: G.(C) -> Unit,
            remove: G.(C) -> Unit,
        ) = object : GtkContainerComposeNode<G, C>(gObject) {
            override fun add(index: Int, child: GtkComposeNode<GObject>) {
                val toReinsert = children.drop(index)
                super.add(index, child)
                toReinsert.forEach { gObject.remove(it) }
                gObject.add(child.gObject as C)
                toReinsert.forEach { gObject.add(it) }
            }

            override fun remove(index: Int) {
                gObject.remove(children[index])
                super.remove(index)
            }
            override fun clear() {
                children.forEach { gObject.remove(it) }
                super.clear()
            }
        }
    }
}

