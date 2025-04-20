package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.GtkContainerComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.Carousel
import org.gnome.adw.SpringParams
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private class AdwCarouselComposeNode(gObject: Carousel) : GtkContainerComposeNode<Carousel>(gObject) {
    override fun add(index: Int, child: GtkComposeWidget<Widget>) {
        when (index) {
            children.size -> widget.append(child.widget)
            0 -> widget.insertAfter(child.widget, null)
            else -> widget.insertAfter(child.widget, children[index - 1])
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
fun Carousel(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
    allowLongSwipes: Boolean = false,
    allowMouseDrag: Boolean = true,
    allowScrollWheel: Boolean = true,
    interactive: Boolean = true,
    revealDuration: Int = 0,
    scrollParams: SpringParams = SpringParams(1.0, 0.5, 500.0),
    spacing: Int = 0,
    onPageChanged: (Int) -> Unit = {},
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeWidget<Carousel>, GtkApplier>(
        factory = { AdwCarouselComposeNode(Carousel()) },
        update = {
            set(modifier) { applyModifier(it) }
            set(orientation) { this.widget.orientation = it }
            set(allowLongSwipes) { this.widget.allowLongSwipes = it }
            set(allowMouseDrag) { this.widget.allowMouseDrag = it }
            set(allowScrollWheel) { this.widget.allowScrollWheel = it }
            set(interactive) { this.widget.interactive = it }
            set(revealDuration) { this.widget.revealDuration = it }
            set(scrollParams) { this.widget.scrollParams = it }
            set(spacing) { this.widget.spacing = it }
            set(onPageChanged) { this.widget.onPageChanged { onPageChanged(it) } }
        },
        content = content,
    )
}