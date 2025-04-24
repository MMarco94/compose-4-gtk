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
    active: Boolean,
    sensitive: Boolean = true,
    onToggle: ((Boolean) -> Unit)? = null,
) {
    ComposeNode<GtkSwitchComposeNode, GtkApplier>({
        val switch = Switch.builder().setActive(active).build()
        GtkSwitchComposeNode(switch)
    }) {
        set(modifier) { applyModifier(it) }
        set(active) {
            this.onStateSet?.block()
            this.widget.active = it
            this.onStateSet?.unblock()
        }
        set(sensitive) { this.widget.sensitive = it }

        set(onToggle) {
            this.onStateSet?.disconnect()

            if (it != null) {
                this.onStateSet = this.widget.onStateSet { newState ->
                    it(newState)
                    if (newState == active) {
                        false
                    }
                    this.onStateSet?.block()
                    this.widget.active = active
                    this.widget.state = active
                    this.onStateSet?.unblock()
                    true
                }
            } else {
                this.onStateSet = null
            }
        }
    }
}
