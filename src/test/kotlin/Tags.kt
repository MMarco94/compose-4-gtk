import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.application
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.margin
import org.gnome.adw.Toast

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", onClose = ::exitApplication) {
            ToastOverlay {
                VerticalBox {
                    HeaderBar()

                    HorizontalClamp {
                        VerticalBox {
                            var text by remember { mutableStateOf("") }
                            Entry(
                                text = text,
                                onTextChange = { text = it },
                                placeholderText = "Inset text here",
                                modifier = Modifier.margin(8),
                            )
                            FlowBox(homogeneous = true) {
                                val tokens = text.split(' ').filter { it.isNotBlank() }
                                tokens.forEach { token ->
                                    Button(token, modifier = Modifier.margin(8)) {
                                        addToast(Toast.builder().setTitle("Clicked on $token").build())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
