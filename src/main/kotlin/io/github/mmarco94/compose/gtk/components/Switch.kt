package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Switch

private class GtkSwitchComposeNode(
    gObject: Switch,
) : LeafComposeNode<Switch>(gObject) {
    var onStateSet: SignalConnection<Switch.StateSetCallback>? = null
}

@Composable
fun Switch(
    modifier: Modifier = Modifier,
    active: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    var pendingChange by remember { mutableStateOf(0) }
    ComposeNode<GtkSwitchComposeNode, GtkApplier>({
        GtkSwitchComposeNode(Switch.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(active to pendingChange) { (active, _) ->
            this.onStateSet?.block()
            this.widget.state = active
            this.widget.active = active
            this.onStateSet?.unblock()
        }

        set(onToggle) {
            this.onStateSet?.disconnect()
            this.onStateSet = this.widget.onStateSet { newState ->
                pendingChange += 1
                it(newState)
                true
            }
        }
    }
}
