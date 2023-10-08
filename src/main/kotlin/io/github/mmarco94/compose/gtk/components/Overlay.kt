package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gobject.GObject
import org.gnome.gtk.Overlay
import org.gnome.gtk.Widget

private class MainChildComposeNode : GtkComposeNode<Nothing?>(null) {
    var child: Widget? = null
    var parent: Overlay? = null
        set(value) {
            parent?.child = null
            field = value
            value?.child = child
        }

    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        require(index == 0)
        require(this.child == null)
        this.child = child.gObject as Widget
        parent?.child = this.child
    }

    override fun remove(index: Int) {
        require(index == 0)
        require(child != null)
        child = null
        parent?.child = this.child
    }

    override fun clear() {
        if (child != null) {
            remove(0)
        }
    }
}

private class OverlaysComposeNode : GtkComposeNode<Nothing?>(null) {
    val children = mutableListOf<Widget>()
    var parent: Overlay? = null
        set(value) {
            children.forEach { parent?.removeOverlay(it) }
            field = value
            children.forEach { value?.addOverlay(it) }
        }

    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        val overlay = child.gObject as Widget
        val toReinsert = children.drop(index)
        toReinsert.forEach { parent?.removeOverlay(it) }
        this.children.add(index, overlay)
        parent?.addOverlay(overlay)
        toReinsert.forEach { parent?.addOverlay(it) }
    }

    override fun remove(index: Int) {
        parent?.removeOverlay(children.removeAt(index))
    }

    override fun clear() {
        children.forEach { parent?.removeOverlay(it) }
        children.clear()
    }
}

private class GtkOverlayComposeNode(gObject: Overlay) : GtkComposeNode<Overlay>(gObject) {
    private var main: MainChildComposeNode? = null
    private var overlays: OverlaysComposeNode? = null
    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        when (index) {
            0 -> {
                require(main == null)
                child as MainChildComposeNode
                child.parent = gObject
                main = child
            }

            1 -> {
                require(overlays == null)
                child as OverlaysComposeNode
                child.parent = gObject
                overlays = child
            }

            else -> throw IllegalStateException()
        }
    }

    override fun remove(index: Int) {
        when (index) {
            0 -> {
                require(main != null)
                main?.parent = null
            }

            1 -> {
                require(overlays != null)
                overlays?.parent = null
            }

            else -> throw IllegalStateException()
        }
    }

    override fun clear() {
        if(main !=null) remove(0)
        if(overlays !=null) remove(1)
    }
}

@Composable
fun Overlay(
    mainChild: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlays: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Overlay>, GtkApplier>(
        factory = {
            GtkOverlayComposeNode(Overlay.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
        },
        content = {
            MainChild {
                mainChild()
            }
            OverlayChildren {
                overlays()
            }
        },
    )
}

@Composable
private fun MainChild(
    content: @Composable () -> Unit,
) {
    ComposeNode<MainChildComposeNode, GtkApplier>(
        factory = {
            MainChildComposeNode()
        },
        update = { },
        content = content,
    )
}

@Composable
private fun OverlayChildren(
    content: @Composable () -> Unit,
) {
    ComposeNode<OverlaysComposeNode, GtkApplier>(
        factory = {
            OverlaysComposeNode()
        },
        update = { },
        content = content,
    )
}