import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.gtk.components.Label
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.cssClasses
import org.gnome.gtk.CssProvider

fun main(args: Array<String>) {
    val style = CssProvider().apply {
        val str = object {}.javaClass.getResourceAsStream("style.css")!!.bufferedReader().readText()
        loadFromString(str)
    }
    application("my.example.HelloApp", args) {
        ApplicationWindow(
            "Styled",
            styles = listOf(style),
            onClose = ::exitApplication,
        ) {
            VerticalBox {
                HeaderBar(Modifier.cssClasses("flat"))
                Label("Big boi", Modifier.cssClasses("big-boi"))
            }
        }
    }
}
