import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.gtk.components.Box
import io.github.mmarco94.compose.gtk.components.Button
import io.github.mmarco94.compose.adw.components.HeaderBar
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow( "Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                Button("Button 1") { println("Clicked!") }
                Button("Button 2") { println("Clicked!") }
                Button("Button 3") { println("Clicked!") }
            }
        }
    }
}
