package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.CheckButton

private class GtkCheckButton(gObject: CheckButton) : SingleChildComposeNode<CheckButton>(gObject, { child = it }) {
    var toggled: SignalConnection<CheckButton.ToggledCallback>? = null
}

/**
 * Internal base function for creating a GTK [CheckButton]. Used by public overloads.
 *
 * This version accepts both a `label` and `child`, but in practice only one should be used.
 *
 * @param modifier The modifier to apply to the widget.
 * @param active Whether the check button is currently active.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param label Optional text label.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param enabled Whether the check button is enabled for interaction.
 * @param child Optional custom composable content.
 * @param onToggle Callback invoked when the check button is toggled.
 */
@Composable
private fun BaseCheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    inconsistent: Boolean = false,
    label: String? = null,
    useUnderline: Boolean = false,
    enabled: Boolean = true,
    child: @Composable () -> Unit = {},
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

/**
 * Creates a [CheckButton] without a label or custom child content.
 *
 * @param modifier The modifier to apply to the widget.
 * @param active Whether the check button is currently active.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param enabled Whether the check button is enabled for interaction.
 * @param onToggle Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    enabled: Boolean = true,
    onToggle: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        useUnderline = useUnderline,
        enabled = enabled,
        onToggle = onToggle,
    )
}

/**
 * Creates a [CheckButton] with a simple text [label].
 *
 * @param modifier The modifier to apply to the widget.
 * @param active Whether the check button is currently active.
 * @param label Text label.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param enabled Whether the check button is enabled for interaction.
 * @param onToggle Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    label: String,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    enabled: Boolean = true,
    onToggle: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        label = label,
        useUnderline = useUnderline,
        enabled = enabled,
        onToggle = onToggle,
    )
}

/**
 * Creates a [CheckButton] with a custom child composable as its content.
 *
 * @param modifier The modifier to apply to the widget.
 * @param active Whether the check button is currently active.
 * @param child Custom composable content.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param enabled Whether the check button is enabled for interaction.
 * @param onToggle Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    child: @Composable () -> Unit,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    enabled: Boolean = true,
    onToggle: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        child = child,
        useUnderline = useUnderline,
        enabled = enabled,
        onToggle = onToggle,
    )
}