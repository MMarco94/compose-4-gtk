package io.github.mmarco94.compose.gtk.components

import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.Toast
import org.gnome.adw.ToastOverlay
import org.gnome.gtk.Widget

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
                add = { toastOverlay.child = it.gObject as Widget },
                remove = { toastOverlay.child = null }
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