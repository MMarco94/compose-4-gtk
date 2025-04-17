import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.gtk.components.Button
import io.github.mmarco94.compose.gtk.components.Label
import io.github.mmarco94.compose.gtk.components.ToggleButton
import io.github.mmarco94.compose.gtk.components.VerticalBox
import kotlin.random.Random

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow("Test", onClose = ::exitApplication) {
            VerticalBox {
                HeaderBar()
                var show by remember { mutableStateOf(false) }
                var allowClose by remember { mutableStateOf(true) }
                var fullscreen by remember { mutableStateOf(false) }
                var title by remember { mutableStateOf("Random window") }

                Button("Show normal") { show = !show }
                ToggleButton("Allow close", allowClose) { allowClose = !allowClose }
                ToggleButton("Fullscreen", fullscreen) { fullscreen = !fullscreen }
                Button("Randomize title") { title = "Random Window ${Random.nextInt()}" }

                if (show) {
                    ApplicationWindow(
                        title = title,
                        fullscreen = fullscreen,
                        onClose = {
                            if (allowClose) {
                                show = false
                            }
                        }
                    ) {
                        VerticalBox {
                            HeaderBar()
                            Label("This is a window!")
                        }
                    }
                }
            }
        }
    }
}
