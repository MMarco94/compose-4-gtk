package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.gtk.components.Image
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.SpinnerPaintable
import org.gnome.adw.StatusPage

@Composable
fun StatusPage(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    iconName: String? = null,
    paintable: Image.Paintable? = null,
    content: @Composable () -> Unit = {},
) {
    ComposeNode<GtkComposeNode<StatusPage>, GtkApplier>(
        factory = {
            SingleChildComposeNode(
                StatusPage.builder().build(),
                set = { child = it },
            )
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(title) { this.gObject.title = it }
            set(description) { this.gObject.description = it }
            set(iconName) { this.gObject.iconName = it }
            set(paintable) {
                (it?.paintable as? SpinnerPaintable)
                    ?.let { spinnerPaintable -> spinnerPaintable.widget = this.gObject }
                this.gObject.paintable = it?.paintable
            }
        },
        content = content,
    )
}