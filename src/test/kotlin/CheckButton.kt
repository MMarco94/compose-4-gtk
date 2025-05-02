import androidx.compose.runtime.*
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.StatusPage
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.alignment
import io.github.mmarco94.compose.modifier.cssClasses
import io.github.mmarco94.compose.modifier.sensitive
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(title = "Check Button", onClose = ::exitApplication) {
            VerticalBox {
                HeaderBar(modifier = Modifier.cssClasses("flat"))

                StatusPage(
                    title = "Check Button", description = "Allow users to control binary options or properties"
                ) {
                    HorizontalBox {
                        VerticalBox(spacing = 16) {
                            var isChecked by remember { mutableStateOf(false) }

                            CheckButton(
                                modifier = Modifier.alignment(Align.START), active = isChecked, label = "Change me!"
                            ) {
                                isChecked = !isChecked
                            }

                            CheckButton(
                                modifier = Modifier.alignment(Align.START), active = isChecked, label = "Mirror only"
                            ) {
                                println("Can't change me!")
                            }

                            CheckButton(
                                modifier = Modifier.alignment(Align.START),
                                active = isChecked,
                                inconsistent = true,
                            ) {
                                println("Nothing happens")
                            }

                            CheckButton(
                                modifier = Modifier.alignment(Align.START),
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

                            CheckButton(
                                modifier = Modifier
                                    .alignment(Align.START)
                                    .sensitive(false),
                                active = isChecked,
                                label = "Disabled"
                            ) {
                                println("Can't change me!")
                            }
                        }

                        VerticalBox(spacing = 16) {
                            val checkedStates = remember { mutableStateListOf(false, false, false, false) }

                            fun allChecked() = checkedStates.all { it }
                            fun someChecked() = checkedStates.any { it }

                            CheckButton(
                                modifier = Modifier.alignment(Align.START),
                                active = allChecked(),
                                inconsistent = someChecked() && !allChecked(),
                                label = "Select all"
                            ) {
                                val newState = !someChecked()
                                for (i in checkedStates.indices) {
                                    checkedStates[i] = newState
                                }
                            }

                            checkedStates.forEachIndexed { index, isChecked ->
                                CheckButton(
                                    modifier = Modifier.alignment(Align.START),
                                    active = isChecked,
                                    label = "Option ${index + 1}"
                                ) {
                                    checkedStates[index] = !checkedStates[index]
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}