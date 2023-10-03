import io.github.mmarco94.compose.application
import io.github.mmarco94.compose.gtk.components.ApplicationWindow
import io.github.mmarco94.compose.gtk.components.Box
import io.github.mmarco94.compose.gtk.components.Button
import io.github.mmarco94.compose.gtk.components.HeaderBar
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                Button("Button 1") { println("Clicked!") }
                Button("Button 2") { println("Clicked!") }
                Button("Button 3") { println("Clicked!") }
            }
        }
    }
}
