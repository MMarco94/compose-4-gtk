import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.application
import io.github.mmarco94.compose.gtk.components.*
import org.gnome.gtk.Button
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                var show by remember { mutableStateOf(false) }
                Button(if (show) "Hide" else "Show") {
                    show = !show
                }
                if (show) {
                    Label("A random label that can be hidden")
                }
            }
        }
    }
}
