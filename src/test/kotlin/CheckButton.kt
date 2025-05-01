import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.StatusPage
import io.github.mmarco94.compose.gtk.components.CheckButton
import io.github.mmarco94.compose.gtk.components.HorizontalBox
import io.github.mmarco94.compose.gtk.components.Label
import io.github.mmarco94.compose.gtk.components.Switch
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.alignment
import io.github.mmarco94.compose.modifier.cssClasses
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(title = "Check Button", onClose = ::exitApplication) {
            VerticalBox {
                HeaderBar(modifier = Modifier.cssClasses("flat"))

                StatusPage(
                    title = "Check Button", description = "Allow users to control binary options or properties"
                ) {
                    VerticalBox(spacing = 16) {
                        var isChecked by remember { mutableStateOf(false) }

                        VerticalBox(spacing = 8) {
                            CheckButton(
                                modifier = Modifier.alignment(Align.CENTER), active = isChecked, label = "Change me!"
                            ) {
                                isChecked = !isChecked
                            }
                        }

                        VerticalBox(spacing = 8) {
                            CheckButton(
                                modifier = Modifier.alignment(Align.CENTER), active = isChecked, label = "Mirror only"
                            ) {
                                println("Can't change me!")
                            }
                        }

                        VerticalBox(spacing = 8) {
                            CheckButton(
                                modifier = Modifier.alignment(Align.CENTER),
                                active = isChecked,
                                inconsistent = true,
                            ) {
                                println("Nothing happens")
                            }
                        }

                        VerticalBox(spacing = 8) {
                            CheckButton(
                                modifier = Modifier.alignment(Align.CENTER),
                                active = isChecked,
                                child = {
                                    HorizontalBox {
                                        Switch(active = isChecked) { }
                                        Label("Custom child")
                                    }
                                },
                            ) {
                                isChecked = !isChecked
                            }
                        }

                        VerticalBox(spacing = 8) {
                            CheckButton(
                                modifier = Modifier.alignment(Align.CENTER),
                                active = isChecked,
                                enabled = false,
                                label = "Disabled"
                            ) {
                                println("Can't change me!")
                            }
                        }
                    }
                }
            }
        }
    }
}