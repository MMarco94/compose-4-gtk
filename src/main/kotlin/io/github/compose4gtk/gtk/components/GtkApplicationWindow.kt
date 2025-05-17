package io.github.compose4gtk.gtk.components

import androidx.compose.runtime.Composable
import io.github.compose4gtk.modifier.Modifier
import io.github.compose4gtk.shared.components.initializeApplicationWindow
import org.gnome.gtk.ApplicationWindow
import org.gnome.gtk.CssProvider

@Composable
fun GtkApplicationWindow(
    title: String?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
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
    init: ApplicationWindow.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    initializeApplicationWindow(
        builder = {
            ApplicationWindow.builder()
        },
        modifier = modifier,
        title = title,
        styles = styles,
        deletable = deletable,
        onClose = onClose,
        decorated = decorated,
        defaultHeight = defaultHeight,
        defaultWidth = defaultWidth,
        fullscreen = fullscreen,
        maximized = maximized,
        handleMenubarAccel = handleMenubarAccel,
        modal = modal,
        resizable = resizable,
        init = init,
        setContent = { this.child = it },
        content = { content() },
    )
}
