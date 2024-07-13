package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.shared.components.WindowInitializer
import io.github.mmarco94.compose.shared.components.initializeApplicationWindow
import org.gnome.adw.ApplicationWindow
import org.gnome.gtk.Application
import org.gnome.gtk.CssProvider

@Composable
fun ApplicationWindow(
    application: Application,
    title: String?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    styles: List<CssProvider> = emptyList(),
    decorated: Boolean = true,
    defaultHeight: Int = 0,
    defaultWidth: Int = 0,
    deletable: Boolean = true,
    fullscreen: Boolean = false,
    handleMenubarAccel: Boolean = true,
    modal: Boolean = false,
    resizable: Boolean = true,
    init: WindowInitializer = {},
    content: @Composable () -> Unit,
) {
    initializeApplicationWindow(
        builder = {
            ApplicationWindow.builder()
        },
        modifier = modifier,
        application = application,
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
        content = content
    )
}