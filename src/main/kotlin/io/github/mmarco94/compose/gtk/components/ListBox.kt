package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.*
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkContainerComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.VirtualComposeNode
import io.github.mmarco94.compose.VirtualComposeNodeContainer
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gobject.GObject
import org.gnome.gtk.Adjustment
import org.gnome.gtk.ListBox
import org.gnome.gtk.SelectionMode
import org.gnome.gtk.Widget

private class GtkListBoxComposeNode(gObject: ListBox) : GtkContainerComposeNode<ListBox, Widget>(gObject) {
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
        // TODO adw 4.12 has removeAll
        children
            .asSequence()
            .filter { it.requiresAddToParent }
            .forEach { gObject.remove(it) }
        super.clear()
    }
}

/**
 * TODO:
 *  - filter func
 *  - header func
 *  - placeholder
 *  - sort func
 *  - selections
 */
@Composable
fun ListBox(
    modifier: Modifier = Modifier,
    activateOnSingleClick: Boolean = true,
    adjustment: Adjustment? = null,
    selectionMode: SelectionMode = SelectionMode.SINGLE,
    showSeparators: Boolean = false,
    placeholder: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<ListBox>, GtkApplier>(
        factory = {
            VirtualComposeNodeContainer(ListBox.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(activateOnSingleClick) { this.gObject.activateOnSingleClick = activateOnSingleClick }
            set(adjustment) { this.gObject.adjustment = adjustment }
            set(selectionMode) { this.gObject.selectionMode = selectionMode }
            set(showSeparators) { this.gObject.showSeparators = showSeparators }
        },
        content = {
            ListBoxPlaceHolder(placeholder)
            ListBoxContent(content)
        },
    )
}

@Composable
private fun ListBoxPlaceHolder(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<ListBox> { listBox ->
                SingleChildComposeNode(listBox) {
                    setPlaceholder(it)
                }
            }
        },
        update = {},
        content = content,
    )
}

@Composable
private fun ListBoxContent(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<ListBox> { listBox ->
                GtkListBoxComposeNode(listBox)
            }
        },
        update = {},
        content = content,
    )
}