package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.shared.components.initializeApplicationWindow
import org.gnome.adw.ApplicationWindow
import org.gnome.adw.Breakpoint
import org.gnome.adw.BreakpointCondition
import org.gnome.gtk.CssProvider

interface ApplicationWindowScope {
    /**
     * This is a very ugly workaround, until https://gitlab.gnome.org/GNOME/libadwaita/-/issues/1018 is solved.
     *
     * Be careful: once added, breakpoints won't be removed. Avoid calling this function dynamically
     */
    @Composable
    fun rememberBreakpoint(
        condition: BreakpointCondition,
        matches: (Boolean) -> Unit,
    )

    @Composable
    fun rememberBreakpoint(
        condition: BreakpointCondition,
    ): State<Boolean> {
        val matches = remember { mutableStateOf(false) }
        rememberBreakpoint(condition) {
            matches.value = it
        }
        return matches
    }
}

private class ApplicationWindowScopeImpl(val window: ApplicationWindow) : ApplicationWindowScope {

    @Composable
    override fun rememberBreakpoint(
        condition: BreakpointCondition,
        matches: (Boolean) -> Unit,
    ): Unit = remember {
        val breakpoint = Breakpoint.builder()
            .setCondition(condition)
            .onApply { matches(true) }
            .onUnapply { matches(false) }
            .build()
        window.addBreakpoint(breakpoint)
    }
}

@Composable
fun ApplicationWindow(
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
    content: @Composable ApplicationWindowScope.() -> Unit,
) {
    initializeApplicationWindow<ApplicationWindow, ApplicationWindow.Builder<*>>(
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
        setContent = { this.content = it },
        content = { window ->
            val scope = remember { ApplicationWindowScopeImpl(window) }
            scope.apply {
                content()
            }
        },
    )
}