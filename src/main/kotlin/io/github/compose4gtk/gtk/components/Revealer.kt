package io.github.compose4gtk.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.compose4gtk.GtkApplier
import io.github.compose4gtk.GtkComposeWidget
import io.github.compose4gtk.SingleChildComposeNode
import io.github.compose4gtk.modifier.Modifier
import org.gnome.gtk.Revealer
import org.gnome.gtk.RevealerTransitionType

@Composable
fun Revealer(
    reveal: Boolean,
    transitionType: RevealerTransitionType = RevealerTransitionType.NONE,
    transitionDuration: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ComposeNode<GtkComposeWidget<Revealer>, GtkApplier>(
        factory = {
            SingleChildComposeNode(Revealer.builder().build()) {
                child = it
            }
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(reveal) { this.widget.revealChild = it }
            set(transitionType) { this.widget.transitionType = it }
            set(transitionDuration) { this.widget.transitionDuration = it }
        },
        content = content,
    )
}
