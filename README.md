# A Kotlin Compose library for Gtk4 and Adw

This library provides a Kotlin Compose interface to interact with native Gtk4 and Adw (Libadwaita) components.

Tl;dr: with this library you can declaratively and dynamically create UIs with the GTK and Adw frameworks.

**At this stage, this is only a proof of concept**

## The basics

An empty window:
```kotlin
fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", onClose = ::exitApplication) {
            // Your content here
        }
    }
}
```
----

A window with an header, and three buttons inside: 
```kotlin
fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", onClose = ::exitApplication) {
            HeaderBar()

            Button("Button 1") { println("Clicked!") }
            Button("Button 2") { println("Clicked!") }
            Button("Button 3") { println("Clicked!") }
        }
    }
}
```

----

An interactive button, that shows or hide a label:
```kotlin
fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                var show by remember { mutableStateOf(false) }
                Button(if (show) "Hide" else "Show") {
                    show = !show
                }
                if (show) {
                    Label("A random label that can be hidden")
                }
            }
        }
    }
}
```

To manage the app's state, this library uses the same principles as Kotlin Compose, see https://developer.android.com/jetpack/compose/state#state-in-composables 

----

An `Entry`, that will make all text uppercase:
```kotlin
fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", onClose = ::exitApplication) {
            Box(orientation = Orientation.VERTICAL) {
                HeaderBar()

                var text by remember { mutableStateOf("") }
                Entry(
                    text = text,
                    placeholderText = "All text will be uppercase",
                    onTextChange = { text = it.uppercase() },
                )
            }
        }
    }
}
```

This example highlights one of the main property of Compose: the state is owned by your app, and not by the GTK Widgets.
This means that the source of truth for what the text should be is the `text` variable. 
See https://developer.android.com/jetpack/compose/state#state-hoisting for more details.

In this  example, at any point the `Entry` could ever contain a text that  is no uppercase. 

----

## Example

![Demo](https://raw.githubusercontent.com/MMarco94/compose-4-gtk-adw/main/screenshots/demo.gif)


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

