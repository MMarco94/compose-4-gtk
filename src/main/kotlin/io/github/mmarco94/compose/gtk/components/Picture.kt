package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.gtk.ImageSource
import io.github.mmarco94.compose.gtk.setPaintable
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.ContentFit
import org.gnome.gtk.Picture

@Composable
fun Picture(
    image: ImageSource.PaintableFactory?,
    modifier: Modifier = Modifier,
    alternativeText: String? = null,
    canShrink: Boolean = true,
    contentFit: ContentFit = ContentFit.CONTAIN,
) {
    ComposeNode<GtkComposeWidget<Picture>, GtkApplier>({
        LeafComposeNode(Picture.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(image) { img ->
            this.widget.setPaintable(
                img?.create(),
                getCurrentPaintable = { paintable },
                setPaintable = { paintable = it },
            )
        }
        set(alternativeText) { this.widget.alternativeText = it }
        set(canShrink) { this.widget.canShrink = it }
        set(contentFit) { this.widget.contentFit = it }
    }
}