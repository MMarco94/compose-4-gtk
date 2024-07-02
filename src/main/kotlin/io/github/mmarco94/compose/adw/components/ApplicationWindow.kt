package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.SingleChildComposeNode
import org.gnome.adw.Application
import org.gnome.adw.ApplicationWindow
import org.gnome.gtk.*


private class GtkApplicationWindowComposeNode(gObject: ApplicationWindow) : SingleChildComposeNode<ApplicationWindow>(
    gObject,
    set = { content = it },
) {
    var styles: List<CssProvider> = emptyList()
        set(value) {
            styles.forEach { StyleContext.removeProviderForDisplay(gObject.display, it) }
            field = value
            styles.forEachIndexed { index, it ->
                StyleContext.addProviderForDisplay(
                    gObject.display,
                    it,
                    Gtk.STYLE_PROVIDER_PRIORITY_FALLBACK + 1 + index,
                )
            }
        }
    var onClose: SignalConnection<Window.CloseRequestCallback>? = null
}


// TODO: fullscreen, maximized, hide on close, icon, active,
@Composable
fun ApplicationWindow(
    application: org.gnome.gtk.Application,
    title: String?,
    onClose: () -> Unit,
    styles: List<CssProvider> = emptyList(),
    decorated: Boolean = true,
    defaultHeight: Int = 0,
    defaultWidth: Int = 0,
    deletable: Boolean = true,
    fullscreen: Boolean = false,
    handleMenubarAccel: Boolean = true,
    modal: Boolean = false,
    resizable: Boolean = true,
    init: ApplicationWindow.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkApplicationWindowComposeNode, GtkApplier>(
        factory = {
            val window = ApplicationWindow.builder()
                .setFullscreened(fullscreen)
                .build()
            window.init()
            GtkApplicationWindowComposeNode(window)
        },
        update = {
            set(application) { this.gObject.application = it }
            set(title) { this.gObject.title = it }
            set(onClose) {
                this.onClose?.disconnect()
                this.onClose = this.gObject.onCloseRequest { it(); true }
            }
            set(styles) { this.styles = it }
            set(decorated) { this.gObject.decorated = it }
            set(defaultHeight to defaultWidth) { (h, w) -> this.gObject.setDefaultSize(w, h) }
            set(deletable) { this.gObject.deletable = it }
            set(fullscreen) {
                val mustChange = it != this.gObject.isFullscreen
                if (mustChange) {
                    this.gObject.fullscreen()
                }
            }
            set(handleMenubarAccel) { this.gObject.handleMenubarAccel = it }
            set(modal) { this.gObject.modal = it }
            set(resizable) { this.gObject.resizable = it }
        },
        content = content,
    )
}