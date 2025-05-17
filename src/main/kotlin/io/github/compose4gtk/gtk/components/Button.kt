package io.github.compose4gtk.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Updater
import io.github.compose4gtk.GtkApplier
import io.github.compose4gtk.GtkComposeWidget
import io.github.compose4gtk.SingleChildComposeNode
import io.github.compose4gtk.modifier.Modifier
import io.github.jwharm.javagi.gobject.SignalConnection
import org.gnome.gtk.Button
import org.gnome.gtk.LinkButton
import org.gnome.gtk.ToggleButton

private class GtkButtonComposeNode(gObject: Button) : SingleChildComposeNode<Button>(gObject, { child = it }) {
    var onClick: SignalConnection<Button.ClickedCallback>? = null
}

private class GtkToggleButtonComposeNode(
    gObject: ToggleButton,
    var onToggle: SignalConnection<ToggleButton.ToggledCallback>,
) : SingleChildComposeNode<ToggleButton>(gObject, { child = it })

private class GtkLinkButtonComposeNode(
    gObject: LinkButton,
    var onActivateLink: SignalConnection<LinkButton.ActivateLinkCallback>?,
) : SingleChildComposeNode<LinkButton>(gObject, { child = it })

@Composable
private fun <B : GtkComposeWidget<Button>> BaseButton(
    creator: () -> B,
    label: String,
    hasFrame: Boolean = true,
    modifier: Modifier = Modifier,
    child: @Composable () -> Unit,
    updater: Updater<B>.() -> Unit,
) {
    ComposeNode<B, GtkApplier>(
        creator,
        update = {
            set(modifier) { applyModifier(it) }
            set(label) { this.widget.label = it }
            set(hasFrame) { this.widget.hasFrame = it }
            updater()
        },
        content = child
    )
}

@Composable
fun Button(
    label: String,
    hasFrame: Boolean = true,
    modifier: Modifier = Modifier,
    child: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    BaseButton(
        creator = { GtkButtonComposeNode(Button.builder().build()) },
        label = label,
        hasFrame = hasFrame,
        modifier = modifier,
        child = child,
        updater = {
            set(onClick) {
                this.onClick?.disconnect()
                this.onClick = this.widget.onClicked(it)
            }
        }
    )
}

@Composable
fun ToggleButton(
    label: String,
    active: Boolean,
    hasFrame: Boolean = true,
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
        hasFrame = hasFrame,
        modifier = modifier,
        child = child,
        updater = {
            set(active) {
                this.onToggle.block()
                this.widget.active = it
                this.onToggle.unblock()
            }
            set(toggled) {
                this.onToggle.disconnect()
                this.onToggle = this.widget.onToggled {
                    toggled()
                    this.onToggle.block()
                    this.widget.active = active
                    this.onToggle.unblock()
                }
            }
        }
    )
}

@Composable
fun IconButton(
    iconName: String,
    hasFrame: Boolean = true,
    modifier: Modifier = Modifier,
    child: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    BaseButton(
        creator = { GtkButtonComposeNode(Button.builder().build()) },
        label = "",
        hasFrame = hasFrame,
        modifier = modifier,
        child = child,
        updater = {
            set(onClick) {
                this.onClick?.disconnect()
                this.onClick = this.widget.onClicked(it)
            }
            set(iconName) { this.widget.iconName = it }
        }
    )
}

@Composable
fun LinkButton(
    label: String,
    uri: String,
    modifier: Modifier = Modifier,
    child: @Composable () -> Unit = {},
    onActivateLink: () -> Boolean = { false },
) {
    BaseButton(
        creator = {
            val lb = LinkButton.builder().build()
            GtkLinkButtonComposeNode(lb, lb.onActivateLink { false })
        },
        label = label,
        modifier = modifier,
        child = child,
        updater = {
            set(uri) { this.widget.uri = it }
            set(onActivateLink) {
                this.onActivateLink?.disconnect()
                this.onActivateLink = this.widget.onActivateLink {
                    onActivateLink()
                }
            }
        }
    )
}
