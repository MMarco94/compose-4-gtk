import io.github.compose4gtk.adw.application
import io.github.compose4gtk.adw.components.ApplicationWindow
import io.github.compose4gtk.adw.components.HeaderBar
import io.github.compose4gtk.adw.components.StatusPage
import io.github.compose4gtk.gtk.components.RadioButton
import io.github.compose4gtk.gtk.components.VerticalBox
import io.github.compose4gtk.gtk.components.rememberRadioGroupState
import io.github.compose4gtk.modifier.Modifier
import io.github.compose4gtk.modifier.cssClasses

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(title = "Radio Buttons", onClose = ::exitApplication) {
            VerticalBox {
                HeaderBar(modifier = Modifier.cssClasses("flat"))

                StatusPage(title = "Radio Buttons", description = "Check buttons grouped to make radio-style buttons") {
                    VerticalBox {
                        val radioOptions = listOf("Calls", "Missed", "Friends")
                        val radioState = rememberRadioGroupState(radioOptions.first())

                        radioOptions.forEach { text ->
                            RadioButton(
                                item = text,
                                state = radioState,
                                label = text
                            ) {
                                radioState.selected = text
                            }
                        }
                    }
                }
            }
        }
    }
}