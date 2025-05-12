import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.compose4gtk.adw.application
import io.github.compose4gtk.adw.components.ApplicationWindow
import io.github.compose4gtk.adw.components.HeaderBar
import io.github.compose4gtk.adw.components.StatusPage
import io.github.compose4gtk.gtk.components.CheckButton
import io.github.compose4gtk.gtk.components.CheckButtonGroup
import io.github.compose4gtk.gtk.components.VerticalBox
import io.github.compose4gtk.modifier.Modifier
import io.github.compose4gtk.modifier.alignment
import io.github.compose4gtk.modifier.cssClasses
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(title = "Radio Buttons", onClose = ::exitApplication) {
            VerticalBox {
                HeaderBar(modifier = Modifier.cssClasses("flat"))

                StatusPage(title = "Radio Buttons", description = "Check buttons grouped to make radio-style buttons") {
                    VerticalBox {
                        val radioOptions = listOf("Calls", "Missed", "Friends")
                        var selectedOption by remember { mutableStateOf(radioOptions[0]) }

                        CheckButtonGroup {
                            radioOptions.forEach { text ->
                                CheckButton(
                                    modifier = Modifier.alignment(Align.START),
                                    active = (text == selectedOption),
                                    label = text,
                                ) {
                                    selectedOption = text
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}