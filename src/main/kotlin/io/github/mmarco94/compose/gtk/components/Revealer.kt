package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
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
            set(transitionType) {this.widget.transitionType = it }
            set(transitionDuration) {this.widget.transitionDuration = it }
        },
        content = content,
    )
}