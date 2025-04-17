package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkContainerComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Box
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private class GtkBoxComposeNode(gObject: Box) : GtkContainerComposeNode<Box, Widget>(gObject) {
    override fun add(index: Int, child: GtkComposeWidget<Widget>) {
        when (index) {
            children.size -> widget.append(child.widget)
            0 -> widget.insertChildAfter(child.widget, null)
            else -> widget.insertChildAfter(child.widget, children[index - 1])
        }
        super.add(index, child)
    }

    override fun remove(index: Int) {
        val child = children[index]
        widget.remove(child)
        super.remove(index)
    }

    override fun clear() {
        children.forEach { widget.remove(it) }
        super.clear()
    }
}

@Composable
fun VerticalBox(
    modifier: Modifier = Modifier,
    spacing: Int = 0,
    homogeneous: Boolean = false,
    content: @Composable () -> Unit,
) {
    Box(modifier, Orientation.VERTICAL, spacing, homogeneous, content)
}

@Composable
fun HorizontalBox(
    modifier: Modifier = Modifier,
    spacing: Int = 0,
    homogeneous: Boolean = false,
    content: @Composable () -> Unit,
) {
    Box(modifier, Orientation.HORIZONTAL, spacing, homogeneous, content)
}

@Composable
fun Box(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
    spacing: Int = 0,
    homogeneous: Boolean = false,
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeWidget<Box>, GtkApplier>(
        factory = {
            GtkBoxComposeNode(Box.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(homogeneous) { this.widget.homogeneous = it }
            set(orientation) { this.widget.orientation = it }
            set(spacing) { this.widget.spacing = it }
        },
        content = content,
    )
}