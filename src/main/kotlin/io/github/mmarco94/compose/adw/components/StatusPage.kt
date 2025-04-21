package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.gtk.ImageSource
import io.github.mmarco94.compose.gtk.setImage
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.StatusPage

@Composable
fun StatusPage(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageSource? = null,
    content: @Composable () -> Unit = {},
) {
    ComposeNode<GtkComposeWidget<StatusPage>, GtkApplier>(
        factory = {
            SingleChildComposeNode(
                StatusPage.builder().build(),
                set = { child = it },
            )
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(title) { this.widget.title = it }
            set(description) { this.widget.description = it }
            set(icon) { img ->
                this.widget.setImage(
                    img,
                    getCurrentPaintable = { this.paintable },
                    setIcon = { this.iconName = it },
                    setPaintable = { this.paintable = it },
                )
            }
        },
        content = content,
    )
}