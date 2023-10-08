package io.github.mmarco94.compose.gtk.components

import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.LeafComposeNode
import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gobject.GObject
import org.gnome.gobject.GObjects
import org.gnome.gtk.Editable
import org.gnome.gtk.Entry
import org.gnome.gtk.InputHints
import org.gnome.gtk.InputPurpose
import org.gnome.pango.AttrList
import org.gnome.pango.TabArray

private class GtkEntryComposeNode(
    gObject: Entry,
    val onDeleteSignalHandler: SignalConnection<Editable.DeleteText>,
    val onInsertSignalHandler: SignalConnection<Editable.InsertText>,
) : LeafComposeNode<Entry>(gObject)

private data class TentativeCursorPosition(
    val position: Int,
    val condition: (String) -> Boolean,
)

private val emptyAttributes = AttrList()

/**
 * TODO:
 *  - setExtraMenu
 *  - Icons
 *  - overwriteMode
 *  - pulse
 */
@Composable
fun Entry(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    attributes: AttrList = emptyAttributes,
    placeholderText: String? = null,
    editable: Boolean = true,
    visibility: Boolean = true,
    activatesDefault: Boolean = false,
    alignment: Float = 0f,
    hasFrame: Boolean = true,
    inputHints: InputHints = InputHints.NONE,
    inputPurpose: InputPurpose = InputPurpose.FREE_FORM,
    invisibleChar: Char? = null,
    maxLength: Int = 0,
    progressFraction: Double = 0.0,
    tabs: TabArray? = null,
    enableUndo: Boolean = true,
    maxWidthChars: Int = -1,
    widthChars: Int = -1,
) {
    val onTextChange by rememberUpdatedState(onTextChange)
    var tentativeCursorPosition by remember { mutableStateOf<TentativeCursorPosition?>(null) }
    ComposeNode<GtkEntryComposeNode, GtkApplier>({
        val entry = Entry.builder().build()
        val editable = entry.delegate!!

        val onInsertText = editable.onInsertText { text, _, position ->
            val prevText = entry.text
            val newText = buildString(prevText.length + text.length) {
                append(prevText)
                insert(position.get(), text)
            }
            onTextChange(newText)
            tentativeCursorPosition = TentativeCursorPosition(
                position = position.get() + text.length,
                condition = { it.length == newText.length },
            )
            GObjects.signalStopEmissionByName(editable as GObject, "insert-text")
        }

        val onDeleteText = editable.onDeleteText { startPos, endPos ->
            val prevText = entry.text
            val newText = when {
                startPos == prevText.length -> prevText
                endPos < 0 -> prevText.removeRange(startPos until text.length)
                else -> prevText.removeRange(startPos until endPos)
            }
            onTextChange(newText)
            tentativeCursorPosition = TentativeCursorPosition(
                position = startPos,
                condition = { it.length == newText.length },
            )
            GObjects.signalStopEmissionByName(editable as GObject, "delete-text")
        }

        GtkEntryComposeNode(entry, onDeleteText, onInsertText)
    }) {
        set(modifier) { applyModifier(it) }
        set(text to tentativeCursorPosition) { (text, pos) ->
            if (this.gObject.text != text) {
                this.onDeleteSignalHandler.block()
                this.onInsertSignalHandler.block()
                this.gObject.text = text
                this.onInsertSignalHandler.unblock()
                this.onDeleteSignalHandler.unblock()
                if (pos != null && pos.condition(text)) {
                    this.gObject.position = pos.position
                }
                tentativeCursorPosition = null
            }
        }
        set(attributes) { this.gObject.attributes = it }
        set(placeholderText) { this.gObject.placeholderText = it }
        set(editable) { this.gObject.editable = it }
        set(visibility) { this.gObject.visibility = it }
        set(activatesDefault) { this.gObject.activatesDefault = it }
        set(alignment) { this.gObject.alignment = it }
        set(hasFrame) { this.gObject.hasFrame = it }
        set(inputHints) { this.gObject.inputHints = it }
        set(inputPurpose) { this.gObject.inputPurpose = it }
        set(invisibleChar) {
            if (it == null) {
                this.gObject.unsetInvisibleChar()
            } else {
                this.gObject.invisibleChar = it.code
            }
        }
        set(maxLength) { this.gObject.maxLength = it }
        set(progressFraction) { this.gObject.progressFraction = it }
        set(tabs) { this.gObject.tabs = it }
        set(enableUndo) { this.gObject.enableUndo = it }
        set(maxWidthChars) { this.gObject.maxWidthChars = it }
        set(widthChars) { this.gObject.widthChars = it }
    }
}