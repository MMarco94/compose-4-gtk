package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.LeafComposeNode
import org.gnome.adw.Banner

private class AdwBannerComposeNode(
    gObject: Banner
) : LeafComposeNode<Banner>(gObject) {
    var onButtonClicked: SignalConnection<Banner.ButtonClickedCallback>? = null
}

@Composable
fun Banner(
    title: String? = null,
    buttonLabel: String? = null,
    revealed: Boolean = true,
    useMarkup: Boolean = true,
    onButtonClicked: (() -> Unit)? = null,
) {
    ComposeNode<AdwBannerComposeNode, GtkApplier>({
        AdwBannerComposeNode(Banner.builder().build())
    }) {
        set(title) { this.gObject.title = it }
        set(buttonLabel) { this.gObject.buttonLabel = it }
        set(revealed) { this.gObject.revealed = it }
        set(useMarkup) { this.gObject.useMarkup = it }
        set(onButtonClicked) {
            this.onButtonClicked?.disconnect()
            if (onButtonClicked != null) {
                this.onButtonClicked = this.gObject.onButtonClicked {
                    onButtonClicked()
                }
            } else {
                this.onButtonClicked = null
            }
        }
    }
}