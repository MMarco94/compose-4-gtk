package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Label

// TODO: all other properties
@Composable
fun Label(
    text: String,
    modifier: Modifier = Modifier,
) {
    ComposeNode<GtkComposeWidget<Label>, GtkApplier>({
        LeafComposeNode(Label.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(text) { this.widget.text = it }
    }
}