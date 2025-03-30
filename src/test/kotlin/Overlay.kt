import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.OverlaySplitView
import io.github.mmarco94.compose.gtk.components.Frame
import io.github.mmarco94.compose.gtk.components.Label
import io.github.mmarco94.compose.gtk.components.ScrolledWindow
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.margin
import org.gnome.gtk.PolicyType

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Overlay", onClose = ::exitApplication) {
            OverlaySplitView(
                sidebar = {}
            ) {
                ScrolledWindow(horizontalScrollbarPolicy = PolicyType.NEVER) {
                    VerticalBox(Modifier.margin(40), spacing = 40) {
                        repeat(10) { index ->
                            Frame {
                                Label("Hello World#$index", Modifier.margin(10))
                            }
                        }
                    }
                }
            }
        }
    }
}
