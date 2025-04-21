import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.gtk.ImageSource
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.alignment
import io.github.mmarco94.compose.modifier.expandVertically
import org.gnome.gio.File
import org.gnome.gtk.Align
import org.gnome.gtk.ContentFit
import org.gnome.gtk.PackType

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow("Test", onClose = ::exitApplication, defaultWidth = 800) {
            VerticalBox {
                Overlay(
                    mainChild = {
                        Picture(
                            ImageSource.forFile(File.newForPath("src/test/gresources/images/lulu.jpg")),
                            contentFit = ContentFit.COVER,
                            modifier = Modifier.expandVertically(),
                        )
                    },
                    overlays = {
                        WindowControls(
                            side = PackType.START,
                            modifier = Modifier.alignment(Align.START, Align.START)
                        )
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
