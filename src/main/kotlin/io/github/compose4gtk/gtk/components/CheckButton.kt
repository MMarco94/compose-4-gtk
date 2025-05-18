package io.github.compose4gtk.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.compose4gtk.GtkApplier
import io.github.compose4gtk.SingleChildComposeNode
import io.github.compose4gtk.modifier.Modifier
import io.github.jwharm.javagi.gobject.SignalConnection
import org.gnome.gtk.CheckButton

private class GtkCheckButtonComposeNode(gObject: CheckButton) :
    SingleChildComposeNode<CheckButton>(gObject, { child = it }) {
    var toggled: SignalConnection<CheckButton.ToggledCallback>? = null
}

sealed interface RadioGroupState

private class RadioGroupStateImpl : RadioGroupState {
    private val checkButtons = mutableListOf<GtkCheckButtonComposeNode>()
    fun registerCheckButton(checkButton: GtkCheckButtonComposeNode) {
        checkButtons.add(checkButton)
        updateGroup()
    }

    fun unregisterCheckButton(checkButton: GtkCheckButtonComposeNode) {
        checkButtons.remove(checkButton)
        updateGroup()
    }

    private fun updateGroup() {
        if (checkButtons.isEmpty()) return

        var prev: GtkCheckButtonComposeNode? = null
        for (checkButton in checkButtons) {
            checkButton.widget.setGroup(prev?.widget)
            prev = checkButton
        }
    }

    fun blockToggledListeners() {
        for (node in checkButtons) {
            node.toggled?.block()
        }
    }

    fun unblockToggledListeners() {
        for (node in checkButtons) {
            node.toggled?.unblock()
        }
    }
}

@Composable
fun rememberRadioGroupState(): RadioGroupState {
    return remember { RadioGroupStateImpl() }
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
 * @param onActiveRequested Callback invoked when the check button is toggled.
 */
@Composable
private fun BaseCheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    state: RadioGroupState? = null,
    inconsistent: Boolean = false,
    label: String? = null,
    useUnderline: Boolean = false,
    child: @Composable () -> Unit = {},
    onActiveRequested: (active: Boolean) -> Unit,
) {
    var pendingChange by remember { mutableStateOf(0) }
    val state: RadioGroupStateImpl? = when (state) {
        is RadioGroupStateImpl -> state
        null -> null
    }
    val checkButtonNode = remember { GtkCheckButtonComposeNode(CheckButton.builder().build()) }

    ComposeNode<GtkCheckButtonComposeNode, GtkApplier>(
        factory = { checkButtonNode },
        update = {
            set(modifier) { this.applyModifier(it) }
            set(active to pendingChange) { (active, _) ->
                this.toggled?.block()
                state?.blockToggledListeners()
                this.widget.active = active
                this.toggled?.unblock()
                state?.unblockToggledListeners()
            }
            set(inconsistent) { this.widget.inconsistent = it }
            set(label) { this.widget.label = it }
            set(useUnderline) { this.widget.useUnderline = it }
            set(onActiveRequested) {
                this.toggled?.disconnect()
                this.toggled = this.widget.onToggled {
                    pendingChange += 1
                    it(this.widget.active)
                    this.toggled?.block()
                    state?.blockToggledListeners()
                    this.widget.active = active
                    this.toggled?.unblock()
                    state?.unblockToggledListeners()
                }
            }
        },
        content = child,
    )

    DisposableEffect(Unit) {
        state?.registerCheckButton(checkButtonNode)
        onDispose {
            state?.unregisterCheckButton(checkButtonNode)
        }
    }
}

/**
 * Creates a [CheckButton] without a label or custom child content.
 *
 * @param modifier The modifier to apply to the widget.
 * @param active Whether the check button is currently active.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param onActiveRequested Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onActiveRequested: (active: Boolean) -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        useUnderline = useUnderline,
        onActiveRequested = onActiveRequested,
    )
}

/**
 * Creates a [RadioButton] without a label or custom child content.
 *
 * @param modifier The modifier to apply to the widget.
 * @param state Shared radio group state for grouping buttons.
 * @param active Whether the check button is currently active.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param onSelect Callback invoked when the radio button is selected.
 */
@Composable
fun RadioButton(
    modifier: Modifier = Modifier,
    state: RadioGroupState,
    active: Boolean,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onSelect: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        state = state,
        active = active,
        inconsistent = inconsistent,
        useUnderline = useUnderline,
        onActiveRequested = { active ->
            if (active) {
                onSelect()
            }
        },
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
 * @param onActiveRequested Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    label: String,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onActiveRequested: (active: Boolean) -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        label = label,
        useUnderline = useUnderline,
        onActiveRequested = onActiveRequested,
    )
}

/**
 * Creates a [RadioButton] with a simple text [label].
 *
 * @param modifier The modifier to apply to the widget.
 * @param state Shared radio group state for grouping buttons.
 * @param active Whether the check button is currently active.
 * @param label Text label.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param onSelect Callback invoked when the radio button is selected.
 */
@Composable
fun RadioButton(
    modifier: Modifier = Modifier,
    state: RadioGroupState,
    active: Boolean,
    label: String,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onSelect: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        state = state,
        active = active,
        inconsistent = inconsistent,
        useUnderline = useUnderline,
        label = label,
        onActiveRequested = { active ->
            if (active) {
                onSelect()
            }
        },
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
 * @param onActiveRequested Callback invoked when the check button is toggled.
 */
@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    child: @Composable () -> Unit,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onActiveRequested: (active: Boolean) -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        active = active,
        inconsistent = inconsistent,
        child = child,
        useUnderline = useUnderline,
        onActiveRequested = onActiveRequested,
    )
}

/**
 * Creates a [RadioButton] with a custom child composable as its content.
 *
 * @param modifier The modifier to apply to the widget.
 * @param state Shared radio group state for grouping buttons.
 * @param active Whether the check button is currently active.
 * @param child Custom composable content.
 * @param inconsistent Whether the button should display an inconsistent (partially active) state.
 * @param useUnderline Whether to use an underscore in the label for mnemonic activation.
 * @param onSelect Callback invoked when the radio button is selected.
 */
@Composable
fun RadioButton(
    modifier: Modifier = Modifier,
    state: RadioGroupState,
    active: Boolean,
    child: @Composable () -> Unit,
    inconsistent: Boolean = false,
    useUnderline: Boolean = false,
    onSelect: () -> Unit,
) {
    BaseCheckButton(
        modifier = modifier,
        state = state,
        active = active,
        inconsistent = inconsistent,
        useUnderline = useUnderline,
        child = child,
        onActiveRequested = { active ->
            if (active) {
                onSelect()
            }
        },
    )
}
