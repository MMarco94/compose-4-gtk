package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.GtkContainerComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.requiresAddToParent
import org.gnome.gobject.GObject
import org.gnome.gtk.Adjustment
import org.gnome.gtk.FlowBox
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private class GtkFlowBoxComposeNode(gObject: FlowBox) : GtkContainerComposeNode<FlowBox, Widget>(gObject) {
    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        val childWidget = child.gObject as Widget
        if (childWidget.requiresAddToParent) {
            gObject.insert(childWidget, index)
        }
        super.add(index, child)
    }

    override fun remove(index: Int) {
        val childWidget = children[index]
        if (childWidget.requiresAddToParent) {
            gObject.remove(childWidget)
        }
        super.remove(index)
    }

    override fun clear() {
        // TODO: use removeAll on GTK 4.12+
        children
            .asSequence()
            .filter { it.requiresAddToParent }
            .forEach { gObject.remove(it) }
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
    ComposeNode<GtkComposeNode<FlowBox>, GtkApplier>(
        factory = {
            GtkFlowBoxComposeNode(FlowBox.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(orientation) { this.gObject.orientation = it }
            set(activateOnSingleClick) { this.gObject.activateOnSingleClick = it }
            set(columnSpacing) { this.gObject.columnSpacing = it }
            set(rowSpacing) { this.gObject.rowSpacing = it }
            set(horizontalAdjustment) { this.gObject.setHadjustment(horizontalAdjustment) }
            set(verticalAdjustment) { this.gObject.setVadjustment(verticalAdjustment) }
            set(homogeneous) { this.gObject.homogeneous = it }
            set(maxChildrenPerLine) { this.gObject.maxChildrenPerLine = it }
            set(minChildrenPerLine) { this.gObject.minChildrenPerLine = it }
        },
        content = content,
    )
}