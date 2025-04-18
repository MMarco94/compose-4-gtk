package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Spinner

@Composable
fun Spinner(
    spinning: Boolean = false,
    modifier: Modifier = Modifier,
) {
    ComposeNode<GtkComposeWidget<Spinner>, GtkApplier>({
        LeafComposeNode(Spinner.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(spinning) { this.widget.spinning = it }
    }
}