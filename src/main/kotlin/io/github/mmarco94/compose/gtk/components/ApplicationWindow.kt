package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.shared.components.initializeApplicationWindow
import org.gnome.gtk.ApplicationWindow
import org.gnome.gtk.CssProvider

@Composable
fun ApplicationWindow(
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
        handleMenubarAccel = handleMenubarAccel,
        modal = modal,
        resizable = resizable,
        init = init,
        setContent = { this.child = it },
        content = content
    )
}