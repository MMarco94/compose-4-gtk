package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.Toast
import org.gnome.adw.ToastOverlay

interface ToastOverlayScope {
    /**
     * Shows a Toast
     * TODO: should the toast be a GTK widget? Probably not
     */
    fun addToast(toast: Toast)
}

private class ToastOverlayScopeImpl : ToastOverlayScope {
    var toastOverlay: ToastOverlay? = null
    override fun addToast(toast: Toast) {
        toastOverlay!!.addToast(toast)
    }
}

@Composable
fun ToastOverlay(
    modifier: Modifier = Modifier,
    content: @Composable ToastOverlayScope.() -> Unit,
) {
    val overlayScope = ToastOverlayScopeImpl()
    ComposeNode<GtkComposeNode<ToastOverlay>, GtkApplier>(
        factory = {
            val toastOverlay = ToastOverlay.builder().build()
            SingleChildComposeNode(
                toastOverlay,
                set = { toastOverlay.child = it },
            )
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(overlayScope) { it.toastOverlay = this.gObject }
        },
        content = {
            overlayScope.content()
        },
    )
}