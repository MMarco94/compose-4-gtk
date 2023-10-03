package io.github.mmarco94.compose.gtk.components

import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.LeafComposeNode
import org.gnome.adw.Application
import org.gnome.adw.ApplicationWindow
import org.gnome.gtk.Widget
import org.gnome.gtk.Window


private class GtkApplicationWindowComposeNode(gObject: ApplicationWindow) : SingleChildComposeNode<ApplicationWindow>(
    gObject,
    add = { content = it as Widget },
    remove = { content = null }
) {
    var onClose: SignalConnection<Window.CloseRequest>? = null
}


// TODO: fullscreen, maximized, hide on close, icon, active,
@Composable
fun ApplicationWindow(
    application: Application,
    title: String?,
    onClose: () -> Unit,
    decorated: Boolean = true,
    defaultHeight: Int = 0,
    defaultWidth: Int = 0,
    deletable: Boolean = true,
    handleMenubarAccel: Boolean = true,
    modal: Boolean = false,
    resizable: Boolean = true,
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkApplicationWindowComposeNode, GtkApplier>(
        factory = {
            val window = ApplicationWindow.builder().build()
            GtkApplicationWindowComposeNode(window)
        },
        update = {
            set(application) { this.gObject.application = it }
            set(title) { this.gObject.title = it }
            set(onClose) {
                this.onClose?.disconnect()
                this.onClose = this.gObject.onCloseRequest { it(); true }
            }
            set(decorated) { this.gObject.decorated = it }
            set(defaultHeight to defaultWidth) { (h, w) -> this.gObject.setDefaultSize(h, w) }
            set(deletable) { this.gObject.deletable = it }
            set(handleMenubarAccel) { this.gObject.handleMenubarAccel = it }
            set(modal) { this.gObject.modal = it }
            set(resizable) { this.gObject.resizable = it }
        },
        content = content,
    )
}