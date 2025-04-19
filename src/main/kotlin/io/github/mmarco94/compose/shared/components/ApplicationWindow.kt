package io.github.mmarco94.compose.shared.components

import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.*
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.ApplicationWindow
import org.gnome.gtk.CssProvider
import org.gnome.gtk.Gtk
import org.gnome.gtk.Widget
import org.gnome.gtk.Window

private class GtkApplicationWindowComposeNode<AW : ApplicationWindow>(
    window: AW,
    set: AW.(Widget?) -> Unit,
) : SingleChildComposeNode<AW>(window, set) {
    var styles: List<CssProvider> = emptyList()
        set(value) {
            styles.forEach { Gtk.styleContextRemoveProviderForDisplay(widget.display, it) }
            field = value
            styles.forEachIndexed { index, it ->
                Gtk.styleContextAddProviderForDisplay(
                    widget.display,
                    it,
                    Gtk.STYLE_PROVIDER_PRIORITY_FALLBACK + 1 + index,
                )
            }
        }
    var onClose: SignalConnection<Window.CloseRequestCallback>? = null
}

val LocalApplicationWindow = compositionLocalOf<ApplicationWindow> {
    throw IllegalStateException("Not inside a window")
}

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
    update: @DisallowComposableCalls Updater<out GtkComposeWidget<AW>>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    val application = LocalApplication.current
    val window = remember {
        builder().build() as AW
    }

    DisposableEffect(Unit) {
        window.init()
        window.application = application
        window.present()
        onDispose { window.destroy() }
    }

    ComposeNode<GtkApplicationWindowComposeNode<AW>, GtkApplier>(
        factory = {
            GtkApplicationWindowComposeNode(window, setContent)
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(title) { this.widget.title = it }
            set(onClose) {
                this.onClose?.disconnect()
                this.onClose = this.widget.onCloseRequest { it(); true }
            }
            set(styles) { this.styles = it }
            set(decorated) { this.widget.decorated = it }
            set(defaultHeight to defaultWidth) { (h, w) -> this.widget.setDefaultSize(w, h) }
            set(deletable) { this.widget.deletable = it }
            set(fullscreen) {
                val mustChange = it != this.widget.isFullscreen
                if (mustChange) {
                    this.widget.fullscreen()
                }
            }
            set(handleMenubarAccel) { this.widget.handleMenubarAccel = it }
            set(modal) { this.widget.modal = it }
            set(resizable) { this.widget.resizable = it }
            this.update()
        },
        content = {
            CompositionLocalProvider(LocalApplicationWindow provides window) {
                content()
            }
        },
    )
}

typealias WindowInitializer = ApplicationWindow.() -> Unit