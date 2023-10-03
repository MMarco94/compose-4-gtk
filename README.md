# Kotlin Compose for Adw and Gtk

This library provides a Kotlin Compose interface to interact with native Gtk and Adw components.

In practice, this will provide a declarative approach to interact with native UIs.

**At this stage, this is only a proof of concept**

## Example

![Demo](https://raw.githubusercontent.com/MMarco94/kotlin-compose-gtk-adw/main/screenshots/demo.gif)


```kotlin
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
```

