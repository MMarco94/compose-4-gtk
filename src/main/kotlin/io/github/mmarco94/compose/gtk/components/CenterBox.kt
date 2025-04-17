package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.*
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.VirtualComposeNode
import io.github.mmarco94.compose.VirtualComposeNodeContainer
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.BaselinePosition
import org.gnome.gtk.CenterBox
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget


@Composable
fun CenterBox(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
    start: @Composable () -> Unit = {},
    end: @Composable () -> Unit = {},
    baselinePosition: BaselinePosition = BaselinePosition.CENTER,
    shrinkCenterLast: Boolean = true,
    center: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeWidget<CenterBox>, GtkApplier>(
        factory = {
            VirtualComposeNodeContainer(CenterBox.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(orientation) { this.widget.orientation = it }
            set(baselinePosition) { this.widget.baselinePosition = it }
            set(shrinkCenterLast) { this.widget.shrinkCenterLast = it }
        },
        content = {
            Child({ startWidget = it }, content = start)
            Child({ centerWidget = it }, content = center)
            Child({ endWidget = it }, content = end)
        },
    )
}

@Composable
private fun Child(
    setter: CenterBox.(Widget?) -> Unit,
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode, GtkApplier>(
        factory = {
            VirtualComposeNode<CenterBox> { overlay ->
                SingleChildComposeNode(
                    overlay,
                    set = { setter(it) },
                )
            }
        },
        update = { },
        content = content,
    )
}