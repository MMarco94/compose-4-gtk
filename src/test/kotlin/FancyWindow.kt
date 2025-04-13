import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.*
import org.gnome.gio.File
import org.gnome.gtk.Align
import org.gnome.gtk.ContentFit
import org.gnome.gtk.PackType

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow("Test", onClose = ::exitApplication, defaultWidth = 800) {
            VerticalBox {
                Overlay(
                    mainChild = {
                        Picture(
                            Image.File(File.newForPath("src/test/resources/random.jpg")),
                            contentFit = ContentFit.COVER,
                            modifier = Modifier.expandVertically(),
                        )
                    },
                    overlays = {
                        WindowControls(side = PackType.START, modifier = Modifier.alignment(Align.START, Align.START))
                        WindowControls(side = PackType.END, modifier = Modifier.alignment(Align.END, Align.START))
                    },
                )
                HeaderBar(
                    startWidgets = { Button("Start") {} },
                    endWidgets = { Button("End") {} },
                    title = {
                        Label("Custom title")
                    }
                )
            }
        }
    }
}
