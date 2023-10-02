package components

import GtkApplier
import GtkComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import org.gnome.gobject.GObject
import org.gnome.gtk.Box
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private class GtkBoxComposeNode(gObject: Box) : GtkComposeNode<Box>(gObject) {
    private val children = mutableListOf<Widget>()
    override fun add(index: Int, child: GObject) {
        println("Adding child at $index")
        child as Widget
        when (index) {
            children.size -> gObject.append(child)
            0 -> gObject.insertChildAfter(child, null)
            else -> gObject.insertChildAfter(child, children[index - 1])
        }
        children.add(index, child)
    }

    override fun remove(index: Int) {
        println("Removing child at $index")
        gObject.remove(children.removeAt(index))
    }

    override fun clear() {
        children.forEach { gObject.remove(it) }
        children.clear()
    }
}

@Composable
fun Box(
    orientation: Orientation = Orientation.VERTICAL,
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Box>, GtkApplier>(
        factory = {
            GtkBoxComposeNode(Box.builder().build())
        },
        update = {
            set(orientation) { this.gObject.orientation = it }
        },
        content = content,
    )
}