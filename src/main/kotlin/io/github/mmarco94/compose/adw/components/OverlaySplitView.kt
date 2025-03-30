package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.*
import io.github.mmarco94.compose.*
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.OverlaySplitView
import org.gnome.gtk.PackType

interface OverlaySplitViewScope {
    val showSidebar: Boolean
    fun showSidebar()
    fun hideSidebar()
}

private class OverlaySplitViewImpl : OverlaySplitViewScope {
    override var showSidebar by mutableStateOf(true)
    var overlaySplitView: OverlaySplitView? = null
        set(widget) {
            require(field == null)
            if (widget != null) {
                widget.showSidebar = showSidebar
                widget.onNotify("show-sidebar") {
                    showSidebar = widget.showSidebar
                }
                field = widget
            }
        }

    override fun showSidebar() {
        showSidebar(true)
    }

    override fun hideSidebar() {
        showSidebar(false)
    }

    private fun showSidebar(show: Boolean) {
        when (val w = overlaySplitView) {
            null -> showSidebar = show
            else -> w.showSidebar = show
        }
    }

}

/**
 * TODO:
 *  - min/max sidebar width
 */
@Composable
fun OverlaySplitView(
    sidebar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    collapsed: Boolean = false,
    pinSidebar: Boolean = false,
    sidebarPosition: PackType = PackType.START,
    sidebarWidthFraction: Double = 0.25,
    enableHideGesture: Boolean = true,
    enableShowGesture: Boolean = true,
    content: @Composable OverlaySplitViewScope.() -> Unit,
) {
    val scope = remember { OverlaySplitViewImpl() }
    ComposeNode<GtkComposeNode<OverlaySplitView>, GtkApplier>(
        factory = {
            val splitView = OverlaySplitView
                .builder()
                .build()
            VirtualComposeNodeContainer(splitView)
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(collapsed) { this.gObject.collapsed = it }
            set(pinSidebar) { this.gObject.pinSidebar = it }
            set(sidebarPosition) { this.gObject.sidebarPosition = it }
            set(sidebarWidthFraction) { this.gObject.sidebarWidthFraction = it }
            set(enableHideGesture) { this.gObject.enableHideGesture = it }
            set(enableShowGesture) { this.gObject.enableShowGesture = it }
            set(scope) { scope.overlaySplitView = this.gObject }
        },
        content = {
            Sidebar {
                sidebar()
            }
            Content {
                scope.apply {
                    content()
                }
            }
        },
    )
}

@Composable
private fun Content(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<OverlaySplitView> { overlay ->
                SingleChildComposeNode(
                    overlay,
                    set = { setContent(it) },
                )
            }
        },
        update = { },
        content = content,
    )
}

@Composable
private fun Sidebar(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<OverlaySplitView> { overlay ->
                SingleChildComposeNode(
                    overlay,
                    set = { sidebar = it },
                )
            }
        },
        update = { },
        content = content,
    )
}
