import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.StatusPage
import io.github.mmarco94.compose.gtk.components.Label
import io.github.mmarco94.compose.gtk.components.Switch
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.alignment
import io.github.mmarco94.compose.modifier.cssClasses
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(title = "Switch", onClose = ::exitApplication) {
            VerticalBox {
                HeaderBar(modifier = Modifier.cssClasses("flat"))

                StatusPage(title = "Switch", description = "A simple on/off control") {
                    VerticalBox(spacing = 16) {
                        var isSwitchActive by remember { mutableStateOf(true) }

                        VerticalBox(spacing = 8) {
                            Switch(
                                modifier = Modifier.alignment(Align.CENTER),
                                active = isSwitchActive,
                            ) { newState ->
                                isSwitchActive = newState
                            }
                            Label(if (isSwitchActive) "On" else "Off")
                        }

                        VerticalBox(spacing = 8) {
                            Switch(
                                modifier = Modifier.alignment(Align.CENTER),
                                active = isSwitchActive,
                            ) { newState ->
                                println("Doesn't change state")
                            }
                            Label("Mirror & read-only")
                        }

                        VerticalBox(spacing = 8) {
                            Switch(
                                modifier = Modifier.alignment(Align.CENTER),
                                active = isSwitchActive,
                                enabled = false,
                            ){}
                            Label("Disabled")
                        }
                    }
                }
            }
        }
    }
}