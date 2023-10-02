package components

import GtkApplier
import GtkComposeNode
import LeafComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import org.gnome.adw.CenteringPolicy
import org.gnome.adw.HeaderBar
import org.gnome.gobject.GObject
import org.gnome.gtk.Button

@Composable
fun HeaderBar(
    centeringPolicy: CenteringPolicy = CenteringPolicy.LOOSE,
) {
    ComposeNode<GtkComposeNode<HeaderBar>, GtkApplier>({
        LeafComposeNode(HeaderBar.builder().build())
    }) {
        set(centeringPolicy) { this.gObject.centeringPolicy = it }
    }
}