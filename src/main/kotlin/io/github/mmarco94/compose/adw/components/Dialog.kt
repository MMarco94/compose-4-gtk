package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.SingleChildComposeNode
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
    updater: Updater<GtkDialogComposeNode<D>>.() -> Unit,
) {
    val applicationWindow = LocalApplicationWindow.current
    val dialog = remember {
        creator()
    }

    DisposableEffect(Unit) {
        dialog.canClose = false
        dialog.present(applicationWindow)
        onDispose {
            dialog.forceClose()
            dialog.emitDestroy()
        }
    }

    ComposeNode<GtkDialogComposeNode<D>, GtkApplier>(
        factory = {
            GtkDialogComposeNode(dialog)
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(title) { this.widget.title = it }
            set(presentationMode) { this.widget.presentationMode = it }
            set(followsContentSize) { this.widget.followsContentSize = followsContentSize }
            set(contentWidth) { this.widget.contentWidth = contentWidth }
            set(contentHeight) { this.widget.contentHeight = contentHeight }
            set(onClose) {
                this.onCloseAttempt?.disconnect()
                this.onCloseAttempt = this.widget.onCloseAttempt {
                    onClose()
                    GObjects.signalStopEmissionByName(dialog, "close-attempt")
                }
            }
            updater()
        },
        content = content,
    )
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
        updater = {},
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
            AboutDialog.builder().build()
        },
        updater = {
            set(applicationIcon) { this.widget.applicationIcon = applicationIcon }
            set(applicationName) { this.widget.applicationName = applicationName }
            set(artists) { this.widget.artists = artists.toTypedArray() }
            set(comments) { this.widget.comments = comments }
            set(copyright) { this.widget.copyright = copyright }
            set(debugInfo) { this.widget.debugInfo = debugInfo }
            set(debugInfoFilename) { this.widget.debugInfoFilename = debugInfoFilename }
            set(designers) { this.widget.designers = designers.toTypedArray() }
            set(developerName) { this.widget.developerName = developerName }
            set(developers) { this.widget.developers = developers.toTypedArray() }
            set(documenters) { this.widget.documenters = documenters.toTypedArray() }
            set(issueUrl) { this.widget.issueUrl = issueUrl }
            set(license) { this.widget.license = license }
            set(licenseType) { this.widget.licenseType = licenseType }
            set(translatorCredits) { this.widget.translatorCredits = translatorCredits }
            set(releaseNotes) { this.widget.releaseNotes = releaseNotes }
            set(releaseNotesVersion) { this.widget.releaseNotesVersion = releaseNotesVersion }
            set(supportUrl) { this.widget.supportUrl = supportUrl }
            set(version) { this.widget.version = version }
            set(website) { this.widget.website = website }
        },
    )
}