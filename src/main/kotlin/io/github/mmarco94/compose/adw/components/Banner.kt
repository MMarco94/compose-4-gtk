package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.Banner

private class AdwBannerComposeNode(
    gObject: Banner
) : LeafComposeNode<Banner>(gObject) {
    var onButtonClicked: SignalConnection<Banner.ButtonClickedCallback>? = null
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    title: String? = null,
    buttonLabel: String? = null,
    revealed: Boolean = true,
    useMarkup: Boolean = true,
    onButtonClicked: (() -> Unit)? = null,
) {
    ComposeNode<AdwBannerComposeNode, GtkApplier>({
        AdwBannerComposeNode(Banner.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(title) { this.widget.title = it }
        set(buttonLabel) { this.widget.buttonLabel = it }
        set(revealed) { this.widget.revealed = it }
        set(useMarkup) { this.widget.useMarkup = it }
        set(onButtonClicked) {
            this.onButtonClicked?.disconnect()
            if (it != null) {
                this.onButtonClicked = this.widget.onButtonClicked {
                    it()
                }
            } else {
                this.onButtonClicked = null
            }
        }
    }
}