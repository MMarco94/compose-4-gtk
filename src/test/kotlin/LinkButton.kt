import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.StatusPage
import io.github.mmarco94.compose.gtk.components.Box
import io.github.mmarco94.compose.gtk.components.LinkButton
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.alignment
import io.github.mmarco94.compose.modifier.cssClasses
import io.github.mmarco94.compose.modifier.margin
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow("Test", onClose = ::exitApplication) {
            VerticalBox {
                HeaderBar()

                StatusPage(title = "Link Button", description = "Links to travel the web") {
                    VerticalBox(spacing = 16) {
                        Box(modifier = Modifier.cssClasses("card").alignment(Align.CENTER)) {
                            VerticalBox(modifier = Modifier.margin(16)) {
                                val uri = "https://github.com/compose4gtk/compose-4-gtk"
                                LinkButton(
                                    "Link to the GitHub repo",
                                    uri = uri
                                )
                            }
                        }


                        Box(modifier = Modifier.cssClasses("card").alignment(Align.CENTER)) {
                            VerticalBox(modifier = Modifier.margin(16)) {
                                val uri = "https://docs.gtk.org/gtk4/"
                                LinkButton(
                                    "Link to the GTK documentation",
                                    uri = uri
                                ) {
                                    println("Visiting $uri")
                                    false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}