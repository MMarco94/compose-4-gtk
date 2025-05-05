import io.github.compose4gtk.adw.application
import io.github.compose4gtk.adw.components.ApplicationWindow
import io.github.compose4gtk.gtk.components.Box
import io.github.compose4gtk.gtk.components.Button
import io.github.compose4gtk.adw.components.HeaderBar
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
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
