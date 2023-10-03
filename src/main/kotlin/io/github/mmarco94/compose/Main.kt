package io.github.mmarco94.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.gtk.components.*
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
                                modifier = Modifier.margin(8)
                            )
                            ScrolledWindow {
                                HorizontalBox {
                                    val tokens = text.split(' ').filter { it.isNotBlank() }
                                    tokens.forEach { token ->
                                        Button(token, modifier = Modifier.margin(8)) {
                                            addToast(Toast.builder().title("Clicked on $token").build())
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
}
