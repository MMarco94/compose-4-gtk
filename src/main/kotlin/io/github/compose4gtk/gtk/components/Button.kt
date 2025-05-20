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
    updater: Updater<B>.() -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hasFrame: Boolean = true,
    child: @Composable () -> Unit,
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    child: @Composable () -> Unit = {},
    hasFrame: Boolean = true,
) {
    BaseButton(
        creator = { GtkButtonComposeNode(Button.builder().build()) },
        label = label,
        modifier = modifier,
        hasFrame = hasFrame,
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
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    hasFrame: Boolean = true,
    child: @Composable () -> Unit = {},
) {
    BaseButton(
        creator = {
            val tb = ToggleButton.builder().build()
            GtkToggleButtonComposeNode(tb, tb.onToggled { })
        },
        label = label,
        modifier = modifier,
        hasFrame = hasFrame,
        child = child,
        updater = {
            set(active) {
                this.onToggle.block()
                this.widget.active = it
                this.onToggle.unblock()
            }
            set(onToggle) {
                this.onToggle.disconnect()
                this.onToggle = this.widget.onToggled {
                    onToggle()
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasFrame: Boolean = true,
    child: @Composable () -> Unit = {},
) {
    BaseButton(
        creator = { GtkButtonComposeNode(Button.builder().build()) },
        label = "",
        modifier = modifier,
        hasFrame = hasFrame,
        child = child,
        updater = {
            set(iconName) { this.widget.iconName = it }
            set(onClick) {
                this.onClick?.disconnect()
                this.onClick = this.widget.onClicked(it)
            }
        }
    )
}

@Composable
fun LinkButton(
    label: String,
    uri: String,
    modifier: Modifier = Modifier,
    onActivateLink: () -> Boolean = { false },
    child: @Composable () -> Unit = {},
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
