import androidx.compose.runtime.*
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.AboutDialog
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.Dialog
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.*
import org.gnome.adw.DialogPresentationMode
import org.gnome.gtk.Align
import org.gnome.gtk.License

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow(
            "Dialogs",
            onClose = ::exitApplication,
            defaultWidth = 960,
            defaultHeight = 800,
        ) {
            var floatingDialog by remember { mutableStateOf(false) }
            Dialog(
                title = "Floating dialog",
                isPresented = floatingDialog,
                presentationMode = DialogPresentationMode.FLOATING,
                onClose = { floatingDialog = false },
            ) {
                VerticalBox {
                    HeaderBar()
                    Label("Say something", modifier = Modifier.margin(64))
                }
            }

            var bottomSheetDialog by remember { mutableStateOf(false) }
            Dialog(
                title = "Bottom sheet dialog",
                isPresented = bottomSheetDialog,
                presentationMode = DialogPresentationMode.BOTTOM_SHEET,
                onClose = { bottomSheetDialog = false },
            ) {
                Label("Say something", modifier = Modifier.margin(64))
            }

            var aboutDialog by remember { mutableStateOf(false) }
            AboutDialog(
                applicationName = "Application",
                applicationIcon = "help-about",
                developerName = "Developer Name",
                artists = arrayOf("Joe Dalton", "Jack Dalton", "William Dalton"),
                designers = arrayOf("Jane Doe", "John Doe"),
                licenseType = License.GPL_3_0,
                issueUrl = "http://www.example.org",
                releaseNotes = "<ul><li>first change</li><li>second change</li></ul>",
                version = "1.2.3",
                website = "http://www.example.org",
                isPresented = aboutDialog,
                title = "About",
                contentWidth = 400,
                contentHeight = 600,
                onClose = { aboutDialog = false },
            )

            VerticalBox {
                HeaderBar()

                VerticalBox(
                    modifier = Modifier.margin(16),
                    spacing = 16,
                ) {
                    Button(
                        modifier = Modifier.horizontalAlignment(Align.CENTER),
                        label = "Floating dialog",
                        onClick = { floatingDialog = true },
                    )
                    Button(
                        modifier = Modifier.horizontalAlignment(Align.CENTER),
                        label = "Bottom sheet dialog",
                        onClick = { bottomSheetDialog = true },
                    )
                    Button(
                        modifier = Modifier.horizontalAlignment(Align.CENTER),
                        label = "About dialog",
                        onClick = { aboutDialog = true },
                    )
                }
            }
        }
    }
}
