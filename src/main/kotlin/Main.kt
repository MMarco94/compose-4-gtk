import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import components.*
import org.gnome.gtk.Orientation

private val splitRegex = "\\s+".toRegex()

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", close = ::exitApplication) {
            Box {
                HeaderBar()

                var text by remember { mutableStateOf("") }

                Entry(text, { text = it })
                Box(orientation = Orientation.HORIZONTAL) {
                    text.split(splitRegex).forEach { token ->
                        Button(token) {
                            println("Clicked on $token")
                        }
                    }
                }
            }
        }
    }
}
