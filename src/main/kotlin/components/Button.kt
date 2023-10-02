package components

import GtkApplier
import GtkComposeNode
import LeafComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import org.gnome.gobject.GObject
import org.gnome.gtk.Button

@Composable
fun Button(
    label: String,
    onClick: () -> Unit,
) {
    //TODO: this could be better achieved by disconnecting the previous signal handler
    val click by rememberUpdatedState(onClick)
    ComposeNode<GtkComposeNode<Button>, GtkApplier>({
        LeafComposeNode<Button>(
            Button
                .builder()
                .build()
                .apply { onClicked { click() } }
        )
    }) {
        set(label) { this.gObject.label = it }
    }
}