import io.github.mmarco94.compose.application
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.*
import org.gnome.gtk.Align
import org.gnome.gtk.PackType

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(application, "Test", onClose = ::exitApplication, defaultWidth = 800) {
            Overlay(
                mainChild = {
                    VerticalBox {
                        repeat(10) {
                            Label("Text #$it")
                        }
                    }
                },
                overlays = {
                    WindowControls(side = PackType.START, modifier = Modifier.alignment(Align.START, Align.START))
                    WindowControls(side = PackType.END, modifier = Modifier.alignment(Align.END, Align.START))
                }
            )
        }
    }
}
