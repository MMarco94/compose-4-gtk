package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.Modifier
import org.gnome.gtk.Label

// TODO: all other properties
@Composable
fun Label(
    text: String,
    modifier: Modifier = Modifier,
) {
    ComposeNode<GtkComposeNode<Label>, GtkApplier>({
        LeafComposeNode(Label.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(text) { this.gObject.text = it }
    }
}