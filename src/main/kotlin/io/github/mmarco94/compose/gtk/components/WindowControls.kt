package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.PackType
import org.gnome.gtk.WindowControls

@Composable
fun WindowControls(
    modifier: Modifier = Modifier,
    side: PackType = PackType.START,
) {
    ComposeNode<GtkComposeWidget<WindowControls>, GtkApplier>({
        LeafComposeNode(WindowControls.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(side) { widget.side = it }
    }
}