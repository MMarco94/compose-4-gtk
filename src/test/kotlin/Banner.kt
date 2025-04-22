import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.Banner
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.HorizontalClamp
import io.github.mmarco94.compose.gtk.components.ToggleButton
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.alignment
import io.github.mmarco94.compose.modifier.expand
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    val bannerTitle =
        "<i><span foreground=\"red\" size=\"x-large\">Hell</span><span foreground=\"green\" size=\"x-large\">o wo</span><span foreground=\"blue\" size=\"x-large\">rld!</span></i>"

    application("my.example.hello-app", args) {
        ApplicationWindow(title = "Banner", onClose = ::exitApplication, defaultWidth = 600, defaultHeight = 400) {
            val isRevealed = remember { mutableStateOf(false) }

            VerticalBox {
                HeaderBar()
                Banner(
                    title = bannerTitle,
                    buttonLabel = "Hide me!",
                    revealed = isRevealed.value,
                    useMarkup = true,
                    onButtonClicked = {
                        isRevealed.value = false
                    })

                HorizontalClamp(modifier = Modifier.expand(true)) {
                    ToggleButton(
                        modifier = Modifier.alignment(Align.CENTER),
                        label = "${if (isRevealed.value) "Hide" else "Reveal"} the banner",
                        active = isRevealed.value,
                        toggled = { isRevealed.value = !isRevealed.value })
                }
            }
        }
    }
}