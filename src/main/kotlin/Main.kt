import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import components.*
import org.gnome.gtk.Orientation

fun main2(args: Array<String>) {
    application("my.example.HelloApp", args) {
        var elements by remember { mutableStateOf(listOf(1, 2, 3, 4, 5, 6)) }
        ApplicationWindow(application, "Test $elements", close = {
            exitApplication()
        }) {
            Box {
                HeaderBar()

                elements.forEach {
                    Button("Button $it") {
                        elements = elements.minus(it)
                    }
                }
                var text by remember { mutableStateOf("abc") }
                Entry(text, { text = it })

                Box(Orientation.HORIZONTAL) {
                    Button("Add") {
                        elements = elements.plus(elements.size + 1)
                    }
                    if (elements.isNotEmpty()) {
                        Button("Remove random") {
                            elements = elements.toMutableList().apply {
                                removeAt(elements.indices.random())
                            }
                        }
                        Button("Remove last") {
                            elements = elements.dropLast(1)
                        }
                    }
                }
            }
        }
    }
}

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
