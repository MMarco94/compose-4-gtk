package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Orientation
import org.gnome.gtk.Separator


@Composable
fun VerticalSeparator(
    modifier: Modifier = Modifier,
) {
    Separator(modifier, Orientation.VERTICAL)
}

@Composable
fun HorizontalSeparator(
    modifier: Modifier = Modifier,
) {
    Separator(modifier, Orientation.HORIZONTAL)
}

@Composable
fun Separator(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
) {
    ComposeNode<GtkComposeNode<Separator>, GtkApplier>({
        LeafComposeNode(Separator.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(orientation) { gObject.orientation = it }
    }
}