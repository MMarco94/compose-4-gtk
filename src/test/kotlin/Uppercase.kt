import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.gtk.components.*
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow("Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                var text by remember { mutableStateOf("") }
                Entry(
                    text = text,
                    placeholderText = "All text will be uppercase",
                    onTextChange = { text = it.uppercase() },
                )
            }
        }
    }
}
