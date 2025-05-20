import io.github.compose4gtk.adw.application
import io.github.compose4gtk.adw.components.ApplicationWindow
import io.github.compose4gtk.gtk.components.Box
import io.github.compose4gtk.gtk.components.Button
import io.github.compose4gtk.adw.components.HeaderBar
import io.github.compose4gtk.modifier.Modifier
import io.github.compose4gtk.modifier.margin
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow("Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                Box(
                    modifier = Modifier.margin(16),
                    orientation = Orientation.VERTICAL,
                    spacing = 16,
                ) {
                    Button(label = "Button", onClick = { println("Clicked!") })
                    Button(label = "Button (no frame)", onClick = { println("Clicked!") }, hasFrame = false)
                }
            }
        }
    }
}
