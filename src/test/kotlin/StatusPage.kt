import androidx.compose.runtime.*
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.StatusPage
import io.github.mmarco94.compose.gtk.components.Image
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.cssClasses
import io.github.mmarco94.compose.modifier.expandVertically
import org.gnome.adw.SpinnerPaintable

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(
            "StatusPage",
            onClose = ::exitApplication,
            defaultWidth = 800,
            defaultHeight = 900,
        ) {
            val spinner: Image.Paintable? by remember { mutableStateOf(Image.Paintable(SpinnerPaintable.builder().build())) }

            VerticalBox {
                HeaderBar()
                StatusPage(
                    title = "Loading",
                    description = "Please wait...",
                    paintable = spinner,
                    modifier = Modifier.expandVertically(),
                )
                StatusPage(
                    title = "No Results Found",
                    description = "Try a different search",
                    iconName = "system-search-symbolic",
                    modifier = Modifier
                        .expandVertically()
                        .cssClasses("compact"),
                )
                StatusPage(
                    title = "No Results Found",
                    description = "Try a different search",
                    iconName = "system-search-symbolic",
                    modifier = Modifier
                        .expandVertically(),
                )
            }
        }
    }
}
