package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.*
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.OverlaySplitView
import org.gnome.gtk.PackType


/**
 * TODO:
 *  - min/max sidebar width
 */
@Composable
fun OverlaySplitView(
    sidebar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    collapsed: Boolean = false,
    showSidebar: Boolean = true,
    sidebarPosition: PackType = PackType.START,
    sidebarWidthFraction: Double = 0.25,
    enableHideGesture: Boolean = true,
    enableShowGesture: Boolean = true,
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<OverlaySplitView>, GtkApplier>(
        factory = {
            VirtualComposeNodeContainer(
                OverlaySplitView
                    .builder()
                    // Set to true, otherwise the sidebar visibility ca drift from `showSidebar`
                    .setPinSidebar(true)
                    .build()
            )
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(collapsed) { this.gObject.collapsed = it }
            set(showSidebar) { this.gObject.showSidebar = it }
            set(sidebarPosition) { this.gObject.sidebarPosition = it }
            set(sidebarWidthFraction) { this.gObject.sidebarWidthFraction = it }
            set(enableHideGesture) { this.gObject.enableHideGesture = it }
            set(enableShowGesture) { this.gObject.enableShowGesture = it }
        },
        content = {
            Sidebar {
                sidebar()
            }
            Content {
                content()
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
