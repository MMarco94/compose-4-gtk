package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.GtkSubComposition
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.shared.components.LocalApplicationWindow
import org.gnome.adw.AboutDialog
import org.gnome.adw.Dialog
import org.gnome.adw.DialogPresentationMode
import org.gnome.gobject.GObjects
import org.gnome.gtk.*

private class GtkDialogComposeNode<D : Dialog>(
    gObject: D,
) : SingleChildComposeNode<D>(gObject, { this.child = it }) {
    var onCloseAttempt: SignalConnection<*>? = null
}

/**
 * TODO:
 *   default widget
 *   focus widget
 *   current breakpoint
 */
@Composable
private fun <D : Dialog> BaseDialog(
    modifier: Modifier = Modifier,
    contentHeight: Int = 0,
    contentWidth: Int = 0,
    followsContentSize: Boolean = false,
    presentationMode: DialogPresentationMode = DialogPresentationMode.AUTO,
    title: String?,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit = {},
    creator: () -> D,
): D {
    val applicationWindow = LocalApplicationWindow.current

    val composeNode = GtkSubComposition(
        createNode = {
            val dialog = creator()
            dialog.canClose = false
            GtkDialogComposeNode(dialog)
        },
        content = { content() },
    )
    val dialog = composeNode.widget

    DisposableEffect(Unit) {
        dialog.present(applicationWindow)
        onDispose {
            dialog.forceClose()
            dialog.emitDestroy()
        }
    }

    remember(modifier) { composeNode.applyModifier(modifier) }
    remember(title) { dialog.title = title }
    remember(presentationMode) { dialog.presentationMode = presentationMode }
    remember(followsContentSize) { dialog.followsContentSize = followsContentSize }
    remember(contentWidth) { dialog.contentWidth = contentWidth }
    remember(contentHeight) { dialog.contentHeight = contentHeight }
    remember(onClose) {
        composeNode.onCloseAttempt?.disconnect()
        composeNode.onCloseAttempt = dialog.onCloseAttempt {
            onClose()
            GObjects.signalStopEmissionByName(dialog, "close-attempt")
        }
    }
    return dialog
}

@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    contentHeight: Int = 0,
    contentWidth: Int = 0,
    followsContentSize: Boolean = false,
    presentationMode: DialogPresentationMode = DialogPresentationMode.AUTO,
    title: String?,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    BaseDialog(
        modifier = modifier,
        contentHeight = contentHeight,
        contentWidth = contentWidth,
        followsContentSize = followsContentSize,
        presentationMode = presentationMode,
        title = title,
        onClose = onClose,
        content = content,
        creator = {
            Dialog.builder().build()
        },
    )
}

@Composable
fun AboutDialog(
    modifier: Modifier = Modifier,
    applicationIcon: String = "",
    applicationName: String,
    artists: List<String> = emptyList(),
    comments: String = "",
    contentHeight: Int = 0,
    contentWidth: Int = 0,
    copyright: String = "",
    debugInfo: String = "",
    debugInfoFilename: String = "",
    designers: List<String> = emptyList(),
    developerName: String = "",
    developers: List<String> = emptyList(),
    documenters: List<String> = emptyList(),
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
    val dialog = BaseDialog(
        modifier = modifier,
        contentHeight = contentHeight,
        contentWidth = contentWidth,
        followsContentSize = followsContentSize,
        presentationMode = presentationMode,
        title = title,
        onClose = onClose,
        content = content,
        creator = {
            AboutDialog.builder().build()
        },
    )
    remember(applicationIcon) { dialog.applicationIcon = applicationIcon }
    remember(applicationName) { dialog.applicationName = applicationName }
    remember(artists) { dialog.artists = artists.toTypedArray() }
    remember(comments) { dialog.comments = comments }
    remember(copyright) { dialog.copyright = copyright }
    remember(debugInfo) { dialog.debugInfo = debugInfo }
    remember(debugInfoFilename) { dialog.debugInfoFilename = debugInfoFilename }
    remember(designers) { dialog.designers = designers.toTypedArray() }
    remember(developerName) { dialog.developerName = developerName }
    remember(developers) { dialog.developers = developers.toTypedArray() }
    remember(documenters) { dialog.documenters = documenters.toTypedArray() }
    remember(issueUrl) { dialog.issueUrl = issueUrl }
    remember(license) { dialog.license = license }
    remember(licenseType) { dialog.licenseType = licenseType }
    remember(translatorCredits) { dialog.translatorCredits = translatorCredits }
    remember(releaseNotes) { dialog.releaseNotes = releaseNotes }
    remember(releaseNotesVersion) { dialog.releaseNotesVersion = releaseNotesVersion }
    remember(supportUrl) { dialog.supportUrl = supportUrl }
    remember(version) { dialog.version = version }
    remember(website) { dialog.website = website }
}