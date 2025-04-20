import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.Carousel
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.StatusPage
import io.github.mmarco94.compose.gtk.components.Box
import io.github.mmarco94.compose.gtk.components.Label
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow("Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                Carousel(
                    spacing = 80,
                    onPageChanged = { index -> println("Page changed to $index") }
                ) {
                    for (i in 1..5) {
                        StatusPage(title = "Test $i") {
                            Label("Status page $i")
                        }
                    }
                }
            }
        }
    }
}