package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Box
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
    ComposeNode<GtkComposeNode<Revealer>, GtkApplier>(
        factory = {
            SingleChildComposeNode(Revealer.builder().build()) {
                child = it
            }
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(reveal) { this.gObject.revealChild = it }
            set(transitionType) {this.gObject.transitionType = it }
            set(transitionDuration) {this.gObject.transitionDuration = it }
        },
        content = content,
    )
}