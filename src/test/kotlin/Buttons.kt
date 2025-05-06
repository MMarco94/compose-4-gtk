import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.gtk.components.Box
import io.github.mmarco94.compose.gtk.components.Button
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.margin
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow( "Test", onClose = ::exitApplication) {
            Box(
                orientation = Orientation.VERTICAL,
            ) {
                HeaderBar()

                Box(
                    modifier = Modifier
                        .margin(16),
                    orientation = Orientation.VERTICAL,
                    spacing = 16,
                ) {
                    Button("Button") { println("Clicked!") }
                    Button("Button (no frame)", hasFrame = false) { println("Clicked!") }
                }
            }
        }
    }
}
