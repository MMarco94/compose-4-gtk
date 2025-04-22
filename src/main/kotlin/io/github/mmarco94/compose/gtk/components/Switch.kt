package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Switch

private class GtkSwitchComposeNode(
    gObject: Switch
) : LeafComposeNode<Switch>(gObject) {
    var onStateSet: SignalConnection<Switch.StateSetCallback>? = null
}

@Composable
fun Switch(
    modifier: Modifier = Modifier,
    active: Boolean = false,
    state: Boolean = active,
    onToggle: ((Boolean) -> Unit)? = null,
) {
    ComposeNode<GtkSwitchComposeNode, GtkApplier>({
        GtkSwitchComposeNode(Switch.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(active) { this.widget.active = it }
        set(state) { this.widget.state = it }
        set(onToggle) {
            this.onStateSet?.disconnect()
            if (it != null) {
                this.onStateSet = this.widget.onStateSet { newState ->
                    it(newState)
                    false
                }
            } else {
                this.onStateSet = null
            }
        }
    }
}