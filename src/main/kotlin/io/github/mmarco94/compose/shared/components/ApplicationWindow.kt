package io.github.mmarco94.compose.shared.components

import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.LocalApplication
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.ApplicationWindow
import org.gnome.gtk.CssProvider
import org.gnome.gtk.Gtk
import org.gnome.gtk.StyleContext
import org.gnome.gtk.Widget
import org.gnome.gtk.Window

private class GtkApplicationWindowComposeNode<AW : ApplicationWindow>(
    gObject: AW,
    set: AW.(Widget?) -> Unit,
) : SingleChildComposeNode<AW>(gObject, set) {
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

val LocalApplicationWindow = compositionLocalOf<ApplicationWindow?> { null }

// TODO: fullscreen, maximized, hide on close, icon, active,
@Composable
fun <AW : ApplicationWindow, B : ApplicationWindow.Builder<*>> initializeApplicationWindow(
    builder: () -> B,
    modifier: Modifier = Modifier,
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
    init: AW.() -> Unit = {},
    setContent: AW.(Widget?) -> Unit,
    update: @DisallowComposableCalls Updater<out GtkComposeNode<AW>>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    val application = LocalApplication.current
    var applicationWindow by remember { mutableStateOf<ApplicationWindow?>(null) }

    CompositionLocalProvider(LocalApplicationWindow provides applicationWindow) {
        ComposeNode<GtkApplicationWindowComposeNode<AW>, GtkApplier>(
            factory = {
                val window = builder()
                    .setFullscreened(fullscreen)
                    .build() as AW
                window.init()
                window.application = application
                applicationWindow = window
                GtkApplicationWindowComposeNode(window, setContent)
            },
            update = {
                set(modifier) { applyModifier(it) }
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
                this.update()
            },
            content = content,
        )
    }
}

typealias WindowInitializer = ApplicationWindow.() -> Unit