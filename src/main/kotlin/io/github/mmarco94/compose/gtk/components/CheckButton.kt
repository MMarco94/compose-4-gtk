package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.CheckButton

private class GtkCheckButton(gObject: CheckButton) : SingleChildComposeNode<CheckButton>(gObject, { child = it }) {
    // var activate: SignalConnection<CheckButton.ActivateCallback>? = null
    var toggled: SignalConnection<CheckButton.ToggledCallback>? = null
}

@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    inconsistent: Boolean = false,
    label: String? = null,
    useUnderline: Boolean = false,
    enabled: Boolean = true,
    child: @Composable () -> Unit = {},
    // onActivate: () -> Unit = {},
    onToggle: () -> Unit,
) {
    var pendingChange by remember { mutableStateOf(0) }
    ComposeNode<GtkCheckButton, GtkApplier>(
        factory = {
            GtkCheckButton(CheckButton.builder().build())
        },
        update = {
            set(modifier) { this.applyModifier(it) }
            set(active to pendingChange) { (active, _) ->
                this.toggled?.block()
                this.widget.active = active
                this.toggled?.unblock()
            }
            set(inconsistent) { this.widget.inconsistent = it }
            set(label) { this.widget.label = it }
            set(useUnderline) { this.widget.useUnderline = it }
            set(enabled) { this.widget.sensitive = it }
            // set(onActivate) {
            //     this.activate?.disconnect()
            //     this.activate = this.widget.onActivate(it)
            // }
            set(onToggle) {
                this.toggled?.disconnect()
                this.toggled = this.widget.onToggled {
                    pendingChange += 1
                    it()
                    this.toggled?.block()
                    this.widget.active = active
                    this.toggled?.unblock()
                }
            }
        },
        content = child
    )
}