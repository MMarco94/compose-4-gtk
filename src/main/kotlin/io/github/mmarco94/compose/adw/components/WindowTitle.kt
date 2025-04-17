package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.WindowTitle

@Composable
fun WindowTitle(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    ComposeNode<GtkComposeWidget<WindowTitle>, GtkApplier>({
        LeafComposeNode(WindowTitle.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(title) { this.widget.title = it }
        set(subtitle) { this.widget.subtitle = it }
    }
}