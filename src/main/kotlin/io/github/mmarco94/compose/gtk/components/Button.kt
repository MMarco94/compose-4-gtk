package io.github.mmarco94.compose.gtk.components

import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Updater
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import org.gnome.gtk.Button
import org.gnome.gtk.ToggleButton
import org.gnome.gobject.GObjects

private class GtkButtonComposeNode(gObject: Button) : SingleChildComposeNode<Button>(gObject, { child = it }) {
    var onClick: SignalConnection<Button.ClickedCallback>? = null
}

private class GtkToggleButtonComposeNode(
    gObject: ToggleButton,
    var onToggle: SignalConnection<ToggleButton.ToggledCallback>,
) : SingleChildComposeNode<ToggleButton>(gObject, { child = it })

@Composable
private fun <B : GtkComposeNode<Button>> BaseButton(
    creator: () -> B,
    label: String,
    modifier: Modifier = Modifier,
    child: @Composable () -> Unit,
    updater: Updater<B>.() -> Unit,
) {
    ComposeNode<B, GtkApplier>(
        creator,
        update = {
            set(modifier) { applyModifier(it) }
            set(label) { this.gObject.label = it }
            updater()
        },
        content = child
    )
}

@Composable
fun Button(
    label: String,
    modifier: Modifier = Modifier,
    child: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    BaseButton(
        creator = { GtkButtonComposeNode(Button.builder().build()) },
        label = label,
        modifier = modifier,
        child = child,
        updater = {
            set(onClick) {
                this.onClick?.disconnect()
                this.onClick = this.gObject.onClicked(it)
            }
        }
    )
}

@Composable
fun ToggleButton(
    label: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    child: @Composable () -> Unit = {},
    toggled: () -> Unit,
) {
    BaseButton(
        creator = {
            val tb = ToggleButton.builder().build()
            GtkToggleButtonComposeNode(tb, tb.onToggled { })
        },
        label = label,
        modifier = modifier,
        child = child,
        updater = {
            set(active) {
                this.onToggle.block()
                this.gObject.active = it
                this.onToggle.unblock()
            }
            set(toggled) {
                this.onToggle.disconnect()
                this.onToggle = this.gObject.onToggled {
                    toggled()
                    this.onToggle.block()
                    this.gObject.active = active
                    this.onToggle.unblock()
                }
            }
        }
    )
}