package io.github.mmarco94.compose.gtk.components

import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.LeafComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.CenteringPolicy
import org.gnome.adw.HeaderBar

@Composable
fun HeaderBar(
    modifier: Modifier = Modifier,
    centeringPolicy: CenteringPolicy = CenteringPolicy.LOOSE,
) {
    ComposeNode<GtkComposeNode<HeaderBar>, GtkApplier>({
        LeafComposeNode(HeaderBar.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(centeringPolicy) { this.gObject.centeringPolicy = it }
    }
}