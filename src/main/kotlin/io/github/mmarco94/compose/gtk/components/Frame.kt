package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.VirtualComposeNode
import io.github.mmarco94.compose.VirtualComposeNodeContainer
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Frame

@Composable
fun Frame(
    modifier: Modifier = Modifier,
    labelXAlign: Float = 0f,
    label: (@Composable () -> Unit)? = null,
    child: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Frame>, GtkApplier>(
        factory = {
            VirtualComposeNodeContainer(Frame.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(labelXAlign) { this.gObject.labelAlign = labelXAlign }
        },
        content = {
            if (label != null) {
                Label(label)
            }
            Child(child)
        },
    )
}

@Composable
private fun Child(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<Frame> { overlay ->
                SingleChildComposeNode(overlay) { child = it }
            }
        },
        update = { },
        content = content,
    )
}

@Composable
private fun Label(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<Frame> { overlay ->
                SingleChildComposeNode(overlay) { labelWidget = it }
            }
        },
        update = { },
        content = content,
    )
}
