package io.github.mmarco94.compose.gtk.components

import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkContainerComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gobject.GObject
import org.gnome.gtk.Box
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private class GtkBoxComposeNode(gObject: Box) : GtkContainerComposeNode<Box, Widget>(gObject) {
    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        val childWidget = child.gObject as Widget
        when (index) {
            children.size -> gObject.append(childWidget)
            0 -> gObject.insertChildAfter(childWidget, null)
            else -> gObject.insertChildAfter(childWidget, children[index - 1])
        }
        super.add(index, child)
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

@Composable
fun VerticalBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier, Orientation.VERTICAL, content)
}

@Composable
fun HorizontalBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier, Orientation.HORIZONTAL, content)
}

@Composable
fun Box(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Box>, GtkApplier>(
        factory = {
            GtkBoxComposeNode(Box.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(orientation) { this.gObject.orientation = it }
        },
        content = content,
    )
}