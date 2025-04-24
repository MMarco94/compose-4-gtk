import androidx.compose.runtime.*
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.*
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.*
import org.gnome.adw.DialogPresentationMode
import org.gnome.adw.ResponseAppearance
import org.gnome.gtk.Align
import org.gnome.gtk.License

private val cancelResponse = AlertDialogResponse(
    id = "cancel",
    label = "_Cancel",
)
private val replaceResponse = AlertDialogResponse(
    id = "replace",
    label = "_Replace",
    appearance = ResponseAppearance.DESTRUCTIVE,
)

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(
            "Dialogs",
            onClose = ::exitApplication,
            defaultWidth = 960,
            defaultHeight = 800,
        ) {
            var floatingDialog by remember { mutableStateOf(false) }
            if (floatingDialog) {
                Dialog(
                    presentationMode = DialogPresentationMode.FLOATING,
                    title = "Floating dialog",
                    onClose = {
                        // Doing nothing, let's keep it open
                    },
                ) {
                    VerticalBox {
                        HeaderBar()
                        Label("Say something", modifier = Modifier.margin(64))
                        Button("close") { floatingDialog = false }
                    }
                }
            }

            var bottomSheetDialog by remember { mutableStateOf(false) }
            if (bottomSheetDialog) {
                Dialog(
                    presentationMode = DialogPresentationMode.BOTTOM_SHEET,
                    title = "Bottom sheet dialog",
                    onClose = { bottomSheetDialog = false },
                ) {
                    Label("Say something", modifier = Modifier.margin(64))
                }
            }

            var aboutDialog by remember { mutableStateOf(false) }
            if (aboutDialog) {
                AboutDialog(
                    applicationName = "Application",
                    applicationIcon = "help-about",
                    developerName = "Developer Name",
                    artists = listOf("Joe Dalton", "Jack Dalton", "William Dalton"),
                    designers = listOf("Jane Doe", "John Doe"),
                    licenseType = License.GPL_3_0,
                    issueUrl = "https://www.example.org",
                    releaseNotes = "<ul><li>first change</li><li>second change</li></ul>",
                    version = "1.2.3",
                    website = "https://www.example.org",
                    title = "About",
                    contentWidth = 400,
                    contentHeight = 600,
                    onClose = { aboutDialog = false },
                )
            }

            var alertDialog by remember { mutableStateOf(false) }
            if (alertDialog) {
                AlertDialog(
                    heading = "Replace file?",
                    body = "A file named \"example.png\" already exists. Do you want to replace it?",
                    followsContentSize = true,
                    responses = listOf(
                        cancelResponse,
                        replaceResponse,
                    ),
                    defaultResponse = cancelResponse,
                    onResponse = { response ->
                        if (response == replaceResponse) {
                            println("Replace the file")
                        }
                    },
                    onClose = {
                        alertDialog = false
                    },
                )
            }

            VerticalBox {
                HeaderBar()

                VerticalBox(
                    modifier = Modifier.margin(16),
                    spacing = 16,
                ) {
                    ToggleButton(
                        label = "Toggle dialog",
                        active = floatingDialog,
                        toggled = { floatingDialog = !floatingDialog }
                    )
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
                    Button(
                        modifier = Modifier.horizontalAlignment(Align.CENTER),
                        label = "Alert dialog",
                        onClick = { alertDialog = true },
                    )
                }
            }
        }
    }
}
