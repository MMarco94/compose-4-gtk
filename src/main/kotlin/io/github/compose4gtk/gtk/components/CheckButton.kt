package io.github.compose4gtk.gtk.components

import androidx.compose.runtime.*
import io.github.compose4gtk.GtkApplier
import io.github.compose4gtk.SingleChildComposeNode
import io.github.compose4gtk.modifier.Modifier
import io.github.jwharm.javagi.gobject.SignalConnection
import org.gnome.gtk.CheckButton

private class GtkCheckButton(gObject: CheckButton) : SingleChildComposeNode<CheckButton>(gObject, { child = it }) {
    var toggled: SignalConnection<CheckButton.ToggledCallback>? = null
}

interface RadioGroupState<T> {
    var selected: T?
    fun registerCheckButton(checkButton: CheckButton)
    fun unregisterCheckButton(checkButton: CheckButton)
}

@Composable
fun <T> rememberRadioGroupState(selected: T?): RadioGroupState<T> {
    var selectedState by remember { mutableStateOf(selected) }
    val checkButtons = remember { mutableStateListOf<CheckButton>() }

    return remember {
        object : RadioGroupState<T> {
            override var selected: T?
                get() = selectedState
                set(value) {
                    if (selectedState != value) {
                        selectedState = value
                    }
                }

            override fun registerCheckButton(checkButton: CheckButton) {
                checkButtons.add(checkButton)
                updateGroup()
            }

            override fun unregisterCheckButton(checkButton: CheckButton) {
                checkButtons.remove(checkButton)
                updateGroup()
            }

            private fun updateGroup() {
                if (checkButtons.isEmpty()) return

                val leader = checkButtons.first()
                for (checkButton in checkButtons.drop(1)) {
                    checkButton.setGroup(leader)
                }
            }
        }
    }
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
    child: @Composable () -> Unit = {},
    onToggle: () -> Unit,
    onCreate: (CheckButton) -> Unit = {},
) {
    var pendingChange by remember { mutableStateOf(0) }

    ComposeNode<GtkCheckButton, GtkApplier>(
        factory = {
            val cb = CheckButton.builder().build()
            onCreate(cb)
            GtkCheckButton(cb)
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
        content = child,
    )
}

/**
 * Creates a [CheckButton] without a label or custom child content.
 *
 * @param modifier The modifier to apply to the widget.
 * @param active Whether the check button is currently active.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param onToggle Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onToggle: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        useUnderline = useUnderline,
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
 * @param onToggle Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    label: String,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onToggle: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        label = label,
        useUnderline = useUnderline,
        onToggle = onToggle,
    )
}

@Composable
fun <T> RadioButton(
    modifier: Modifier = Modifier,
    item: T,
    state: RadioGroupState<T>,
    label: String,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onToggle: () -> Unit,
) {
    var gtkCheckButton by remember { mutableStateOf<CheckButton?>(null) }

    BaseCheckButton(
        modifier = modifier,
        active = (state.selected == item),
        inconsistent = inconsistent,
        useUnderline = useUnderline,
        label = label,
        onToggle = onToggle,
        onCreate = { checkButton -> gtkCheckButton = checkButton }
    )

    DisposableEffect(gtkCheckButton) {
        val cb = gtkCheckButton
        if (cb != null) {
            state.registerCheckButton(cb)
        }
        onDispose {
            if (cb != null) {
                state.unregisterCheckButton(cb)
            }
        }
    }
}

/**
 * Creates a [CheckButton] with a custom child composable as its content.
 *
 * @param modifier The modifier to apply to the widget.
 * @param active Whether the check button is currently active.
 * @param child Custom composable content.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param onToggle Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    child: @Composable () -> Unit,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onToggle: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        child = child,
        useUnderline = useUnderline,
        onToggle = onToggle,
    )
}