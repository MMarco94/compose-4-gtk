# Kotlin Compose for Adw and Gtk

This library provides a Kotlin Compose interface to interact with native Gtk and Adw components.

In practice, this will provide a declarative approach to interact with native UIs.

**At this stage, this is only a proof of concept**

## Example syntax

This creates a window with an header and three buttons:

```kotlin
fun main(args: Array<String>) = application("my.example.HelloApp", args) {
    ApplicationWindow(application, "My window", close = ::exitApplication) {
        Box {
            HeaderBar()

            repeat(3) {
                Button("Button $it") {
                    println("Clicked button $it")
                }
            }
        }
    }
}
```

