import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.sizeRequest
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow("Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                var show by remember { mutableStateOf(false) }
                ToggleButton("Show  text", show) {
                    show = !show
                }
                if (show) {
                    Label("A random label that can be hidden", modifier = Modifier.sizeRequest(300, 150))
                }
            }
        }
    }
}
