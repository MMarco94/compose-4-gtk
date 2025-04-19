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
import org.gnome.gtk.Adjustment
import org.gnome.gtk.ListBox
import org.gnome.gtk.SelectionMode
import org.gnome.gtk.Widget

private class GtkListBoxComposeNode(gObject: ListBox) : GtkContainerComposeNode<ListBox>(gObject) {
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
    ComposeNode<GtkComposeWidget<ListBox>, GtkApplier>(
        factory = {
            VirtualComposeNodeContainer(ListBox.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(activateOnSingleClick) { this.widget.activateOnSingleClick = activateOnSingleClick }
            set(adjustment) { this.widget.adjustment = adjustment }
            set(selectionMode) { this.widget.selectionMode = selectionMode }
            set(showSeparators) { this.widget.showSeparators = showSeparators }
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
    ComposeNode<GtkComposeNode, GtkApplier>(
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
    ComposeNode<GtkComposeNode, GtkApplier>(
        factory = {
            VirtualComposeNode<ListBox> { listBox ->
                GtkListBoxComposeNode(listBox)
            }
        },
        update = {},
        content = content,
    )
}