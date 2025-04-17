package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.*
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkContainerComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Adjustment
import org.gnome.gtk.FlowBox
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private class GtkFlowBoxComposeNode(gObject: FlowBox) : GtkContainerComposeNode<FlowBox, Widget>(gObject) {
    override fun add(index: Int, child: GtkComposeWidget<Widget>) {
        widget.insert(child.widget, index)
        super.add(index, child)
    }

    override fun remove(index: Int) {
        widget.remove(children[index])
        super.remove(index)
    }

    override fun clear() {
        widget.removeAll()
        super.clear()
    }
}

private val nullAdjustment = Adjustment.builder().build()

/**
 * TODO:
 *  - model
 *  - select
 *  - filter?
 *  - sort?
 */
@Composable
fun FlowBox(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
    activateOnSingleClick: Boolean = true,
    columnSpacing: Int = 0,
    rowSpacing: Int = 0,
    horizontalAdjustment: Adjustment = nullAdjustment,
    verticalAdjustment: Adjustment = nullAdjustment,
    homogeneous: Boolean = false,
    maxChildrenPerLine: Int = 7,
    minChildrenPerLine: Int = 0,
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeWidget<FlowBox>, GtkApplier>(
        factory = {
            GtkFlowBoxComposeNode(FlowBox.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(orientation) { this.widget.orientation = it }
            set(activateOnSingleClick) { this.widget.activateOnSingleClick = it }
            set(columnSpacing) { this.widget.columnSpacing = it }
            set(rowSpacing) { this.widget.rowSpacing = it }
            set(horizontalAdjustment) { this.widget.setHadjustment(horizontalAdjustment) }
            set(verticalAdjustment) { this.widget.setVadjustment(verticalAdjustment) }
            set(homogeneous) { this.widget.homogeneous = it }
            set(maxChildrenPerLine) { this.widget.maxChildrenPerLine = it }
            set(minChildrenPerLine) { this.widget.minChildrenPerLine = it }
        },
        content = content,
    )
}