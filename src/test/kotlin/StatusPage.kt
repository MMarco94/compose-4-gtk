import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.StatusPage
import io.github.mmarco94.compose.gtk.ImageSource
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.cssClasses
import io.github.mmarco94.compose.modifier.expandVertically

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(
            "StatusPage",
            onClose = ::exitApplication,
            defaultWidth = 800,
            defaultHeight = 900,
        ) {
            VerticalBox {
                HeaderBar()
                StatusPage(
                    title = "Loading",
                    description = "Please wait...",
                    icon = ImageSource.spinner,
                    modifier = Modifier.expandVertically(),
                )
                StatusPage(
                    title = "No Results Found",
                    description = "Try a different search",
                    icon = ImageSource.Icon("system-search-symbolic"),
                    modifier = Modifier
                        .expandVertically()
                        .cssClasses("compact"),
                )
                StatusPage(
                    title = "No Results Found",
                    description = "Try a different search",
                    icon = ImageSource.Icon("system-search-symbolic"),
                    modifier = Modifier
                        .expandVertically(),
                )
            }
        }
    }
}
