import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.gtk.components.Label
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.alignment
import io.github.mmarco94.compose.modifier.cssClasses
import io.github.mmarco94.compose.modifier.expandVertically
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(
            "compose-4-gtk",
            onClose = ::exitApplication,
            defaultWidth = 600,
            defaultHeight = 400,
        ) {
            VerticalBox {
                HeaderBar(Modifier.cssClasses("flat"))
                Label(
                    "Kotlin + GTK + ADW = ❤",
                    Modifier.expandVertically().alignment(Align.CENTER).cssClasses("title-1"),
                )
            }
        }
    }
}
