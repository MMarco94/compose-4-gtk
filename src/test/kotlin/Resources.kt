import androidx.compose.runtime.*
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.gtk.ImageSource
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.cssClasses
import io.github.mmarco94.compose.modifier.margin
import io.github.mmarco94.compose.modifier.verticalAlignment
import io.github.mmarco94.compose.useGioResource
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    val possibleIcons = setOf(
        // Custom icons
        ImageSource.Icon("heart-filled-symbolic"),
        ImageSource.Icon("cat-symbolic"),
        ImageSource.forResource("/my/example/hello-app/images/lulu.jpg"),
        // Default icons
        ImageSource.Icon("system-search-symbolic"),
        ImageSource.spinner,
        null,
    )
    useGioResource("resources.gresource") {
        application("my.example.hello-app", args) {
            ApplicationWindow(
                "Embedded resources",
                onClose = ::exitApplication,
            ) {
                VerticalBox {
                    var icon by remember { mutableStateOf(possibleIcons.first()) }
                    HeaderBar(Modifier.cssClasses("flat"))
                    Row("Embedded icon") {
                        Image(
                            icon,
                            iconSize = ImageSize.Normal,
                        )
                    }
                    Row("Accent icon") {
                        Image(
                            icon,
                            iconSize = ImageSize.Large,
                            modifier = Modifier.cssClasses("accent-colored"),
                        )
                    }
                    Row("Big") {
                        Image(
                            icon,
                            iconSize = ImageSize.Specific(sizePx = 96),
                        )
                    }
                    Row("Styled text") {
                        Label("Big boi", Modifier.cssClasses("big-boi", "accent-colored"))
                    }
                    Button("Randomize") {
                        icon = (possibleIcons - icon).random()
                    }

                    Label(
                        "The stylesheet and the icons are declared in the test/gresources directory",
                        Modifier.margin(24).cssClasses("dimmed"),
                    )
                }
            }
        }
    }
}

@Composable
fun Row(label: String, content: @Composable () -> Unit) {
    HorizontalBox {
        Label("$label:", Modifier.verticalAlignment(Align.CENTER))
        content()
    }
}
