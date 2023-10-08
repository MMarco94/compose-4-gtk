package io.github.mmarco94.compose.gtk.components

import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.jwharm.javagi.gobject.SignalConnection
import org.gnome.gtk.Button

private class GtkButtonComposeNode(gObject: Button) : LeafComposeNode<Button>(gObject) {
    var onClick: SignalConnection<Button.Clicked>? = null
}

@Composable
fun Button(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ComposeNode<GtkButtonComposeNode, GtkApplier>({
        GtkButtonComposeNode(Button.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(label) { this.gObject.label = it }
        set(onClick) {
            this.onClick?.disconnect()
            this.onClick = this.gObject.onClicked(it)
        }
    }
}