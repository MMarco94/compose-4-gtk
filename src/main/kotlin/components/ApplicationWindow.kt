package components

import GtkApplier
import GtkComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import org.gnome.adw.Application
import org.gnome.adw.ApplicationWindow
import org.gnome.gobject.GObject
import org.gnome.gtk.Widget

private class GtkApplicationWindowComposeNode(
    gObject: ApplicationWindow
) : GtkComposeNode<ApplicationWindow>(gObject) {
    override fun add(index: Int, child: GObject) {
        require(index == 0)
        println("Setting window content")
        gObject.content = child as Widget
    }

    override fun remove(index: Int) {
        require(index == 0)
        println("Removing window content")
        gObject.content = null
    }

    override fun clear() {
        gObject.content = null
    }
}

@Composable
fun ApplicationWindow(
    application: Application,
    title: String?,
    close: () -> Unit,
    content: @Composable () -> Unit,
) {
    val onClose by rememberUpdatedState(close)
    ComposeNode<GtkComposeNode<ApplicationWindow>, GtkApplier>(
        factory = {
            GtkApplicationWindowComposeNode(
                ApplicationWindow.builder()
                    .build()
                    .also { window ->
                        window.setDefaultSize(800, 480)
                        window.onCloseRequest { onClose(); true }
                    }
            )
        },
        update = {
            set(application) { this.gObject.application = it }
            set(title) { this.gObject.title = it }
        },
        content = content,
    )
}