import androidx.compose.runtime.AbstractApplier
import org.gnome.gobject.GObject
import org.gnome.gtk.Widget

abstract class GtkComposeNode<out G : GObject>(
    val gObject: G,
) {

    abstract fun add(index: Int, child: GObject)
    abstract fun remove(index: Int)
    abstract fun clear()
}

open class LeafComposeNode<G : Widget>(gObject: G) : GtkComposeNode<G>(gObject) {
    override fun add(index: Int, child: GObject) = throw UnsupportedOperationException()
    override fun remove(index: Int) = throw UnsupportedOperationException()
    override fun clear() = throw UnsupportedOperationException()
}

class GtkApplier(root: GtkComposeNode<GObject>) : AbstractApplier<GtkComposeNode<GObject>>(root) {
    override fun insertBottomUp(index: Int, instance: GtkComposeNode<GObject>) = Unit
    override fun insertTopDown(index: Int, instance: GtkComposeNode<GObject>) {
        current.add(index, instance.gObject)
    }

    override fun remove(index: Int, count: Int) {
        repeat(count) {
            current.remove(index)
        }
    }

    override fun move(from: Int, to: Int, count: Int) {
        TODO("Move not yet implemented")
    }

    override fun onClear() {
        current.clear()
    }

}