package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Updater
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.shared.components.LocalApplicationWindow
import org.gnome.adw.AboutDialog
import org.gnome.adw.Dialog
import org.gnome.adw.DialogPresentationMode
import org.gnome.gtk.License

/**
 * TODO:
 *   default widget
 *   focus widget
 *   current breakpoint
 */
@Composable
private fun <D : GtkComposeNode<Dialog>> BaseDialog(
    modifier: Modifier = Modifier,
    canClose: Boolean = true,
    contentHeight: Int = 0,
    contentWidth: Int = 0,
    isPresented: Boolean,
    followsContentSize: Boolean = false,
    presentationMode: DialogPresentationMode = DialogPresentationMode.AUTO,
    title: String?,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit = {},
    creator: () -> D,
    updater: Updater<D>.() -> Unit,
) {
    val applicationWindow = LocalApplicationWindow.current

    ComposeNode<D, GtkApplier>(
        factory = creator,
        update = {
            set(modifier) { applyModifier(it) }
            set(title) { this.gObject.title = it }
            set(presentationMode) { this.gObject.presentationMode = it }
            set(canClose) { this.gObject.canClose = it }
            set(followsContentSize) { this.gObject.followsContentSize = followsContentSize }
            set(contentWidth) { this.gObject.contentWidth = contentWidth }
            set(contentHeight) { this.gObject.contentHeight = contentHeight }
            set(isPresented) {
                when {
                    isPresented ->
                        if (applicationWindow != null) {
                            this.gObject.present(applicationWindow)
                        }
                    this.gObject.canClose ->
                        this.gObject.close()
                    else ->
                        this.gObject.forceClose()
                }
            }
            set(onClose) { this.gObject.onClosed { onClose() } }
            updater()
        },
        content = content,
    )
}

@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    canClose: Boolean = true,
    contentHeight: Int = 0,
    contentWidth: Int = 0,
    isPresented: Boolean,
    followsContentSize: Boolean = false,
    presentationMode: DialogPresentationMode = DialogPresentationMode.AUTO,
    title: String?,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    BaseDialog(
        modifier = modifier,
        title = title,
        presentationMode = presentationMode,
        canClose = canClose,
        followsContentSize = followsContentSize,
        contentWidth = contentWidth,
        contentHeight = contentHeight,
        isPresented = isPresented,
        onClose = onClose,
        content = content,
        creator = {
            SingleChildComposeNode(
                Dialog.builder().build(),
                set = { child = it },
            )
        },
        updater = {},
    )
}

@Composable
fun AboutDialog(
    modifier: Modifier = Modifier,
    applicationIcon: String = "",
    applicationName: String,
    artists: Array<String> = emptyArray(),
    canClose: Boolean = true,
    comments: String = "",
    contentHeight: Int = 0,
    contentWidth: Int = 0,
    copyright: String = "",
    debugInfo: String = "",
    debugInfoFilename: String = "",
    designers: Array<String> = emptyArray(),
    developerName: String = "",
    developers: Array<String> = emptyArray(),
    documenters: Array<String> = emptyArray(),
    isPresented: Boolean,
    issueUrl: String = "",
    followsContentSize: Boolean = false,
    license: String = "",
    licenseType: License = License.UNKNOWN,
    presentationMode: DialogPresentationMode = DialogPresentationMode.AUTO,
    releaseNotes: String = "",
    releaseNotesVersion: String = "",
    supportUrl: String = "",
    title: String?,
    translatorCredits: String = "",
    version: String = "",
    website: String = "",
    onClose: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    BaseDialog(
        modifier = modifier,
        canClose = canClose,
        presentationMode = presentationMode,
        title = title,
        isPresented = isPresented,
        contentWidth = contentWidth,
        contentHeight = contentHeight,
        followsContentSize = followsContentSize,
        content = content,
        onClose = onClose,
        creator = {
            SingleChildComposeNode(
                AboutDialog.builder().build(),
                set = { child = it },
            )
        },
        updater = {
            set(applicationIcon) { this.gObject.applicationIcon = applicationIcon }
            set(applicationName) { this.gObject.applicationName = applicationName }
            set(artists) { this.gObject.artists = artists }
            set(comments) { this.gObject.comments = comments }
            set(copyright) { this.gObject.copyright = copyright }
            set(debugInfo) { this.gObject.debugInfo = debugInfo }
            set(debugInfoFilename) { this.gObject.debugInfoFilename = debugInfoFilename }
            set(designers) { this.gObject.designers = designers }
            set(developerName) { this.gObject.developerName = developerName }
            set(developers) { this.gObject.developers = developers }
            set(documenters) { this.gObject.documenters = documenters }
            set(issueUrl) { this.gObject.issueUrl = issueUrl }
            set(license) { this.gObject.license = license }
            set(licenseType) { this.gObject.licenseType = licenseType }
            set(translatorCredits) { this.gObject.translatorCredits = translatorCredits }
            set(releaseNotes) { this.gObject.releaseNotes = releaseNotes }
            set(releaseNotesVersion) { this.gObject.releaseNotesVersion = releaseNotesVersion }
            set(supportUrl) { this.gObject.supportUrl = supportUrl }
            set(version) { this.gObject.version = version }
            set(website) { this.gObject.website = website }
        },
    )
}