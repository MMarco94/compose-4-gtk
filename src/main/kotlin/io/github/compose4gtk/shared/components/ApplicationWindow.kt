package io.github.compose4gtk.shared.components

import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.compose4gtk.*
import io.github.compose4gtk.SingleChildComposeNode
import io.github.compose4gtk.modifier.Modifier
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
internal fun <AW : ApplicationWindow, B : ApplicationWindow.Builder<*>> initializeApplicationWindow(
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
    maximized: Boolean = false,
    handleMenubarAccel: Boolean = true,
    modal: Boolean = false,
    resizable: Boolean = true,
    init: AW.() -> Unit = {},
    setContent: AW.(Widget?) -> Unit,
    content: @Composable (AW) -> Unit,
) {
    val application = LocalApplication.current
    val composeNode = GtkSubComposition(
        createNode = {
            val window = builder().build() as AW
            GtkApplicationWindowComposeNode(window, setContent)
        },
        content = { composeNode ->
            CompositionLocalProvider(LocalApplicationWindow provides composeNode.widget) {
                content(composeNode.widget)
            }
        }
    )
    val window = composeNode.widget

    DisposableEffect(Unit) {
        window.init()
        window.application = application
        window.present()
        onDispose {
            window.destroy()
        }
    }
    remember(modifier) { composeNode.applyModifier(modifier) }
    remember(title) { window.title = title }
    remember(onClose) {
        composeNode.onClose?.disconnect()
        composeNode.onClose = window.onCloseRequest { onClose(); true }
    }
    remember(styles) { composeNode.styles = styles }
    remember(decorated) { window.decorated = decorated }
    remember(defaultWidth to defaultHeight) { window.setDefaultSize(defaultWidth, defaultHeight) }
    remember(deletable) { window.deletable = deletable }
    remember(fullscreen) {
        val mustChange = fullscreen != window.isFullscreen
        if (mustChange) {
            if (fullscreen) {
                window.fullscreen()
            } else {
                window.unfullscreen()
            }
        }
    }
    remember(maximized) {
        val mustChange = maximized != window.isMaximized
        if (mustChange) {
            if (maximized) {
                window.maximize()
            } else {
                window.unmaximize()
            }
        }
    }
    remember(handleMenubarAccel) { window.handleMenubarAccel = handleMenubarAccel }
    remember(modal) { window.modal = modal }
    remember(resizable) { window.resizable = resizable }
}
