package components

import GtkApplier
import LeafComposeNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.jwharm.javagi.gobject.SignalConnection
import org.gnome.gtk.Editable
import org.gnome.gtk.Entry

private class GtkEntryComposeNode(
    gObject: Entry,
    val onDeleteSignalHandler: SignalConnection<Editable.DeleteText>,
    val onInsertSignalHandler: SignalConnection<Editable.InsertText>,
) : LeafComposeNode<Entry>(gObject)

@Composable
fun Entry(
    text: String,
    onTextChange: (String) -> Unit,
    placeholderText: String? = null,
) {
    val onTextChange by rememberUpdatedState(onTextChange)
    ComposeNode<GtkEntryComposeNode, GtkApplier>({
        val entry = Entry.builder().build()
        val editable = entry.delegate!!

        val onInsertText = editable.onInsertText { text, length, position ->
            println("On insert $text")
        }.apply { block() } // TODO: this causes memory out of bounds

        val onDeleteText = editable.onDeleteText { startPos, endPos ->
            val prevText = entry.text
            val newText = when {
                startPos == prevText.length -> prevText
                endPos < 0 -> prevText.removeRange(startPos until text.length)
                else -> prevText.removeRange(startPos until endPos)
            }
            println("On delete. Prev = ${prevText}; new = $newText")
            onTextChange(newText)
            //GObjects.signalStopEmissionByName(editable as GObject, "delete-text")
        }

        GtkEntryComposeNode(entry, onDeleteText, onInsertText)
    }) {
        set(text) {
            if (this.gObject.text != it) {
                val prevPos = this.gObject.position
                this.onDeleteSignalHandler.block()
                this.onInsertSignalHandler.block()
                this.gObject.text = it
                this.onInsertSignalHandler.unblock()
                this.onDeleteSignalHandler.unblock()
                this.gObject.position = prevPos
            }
        }
        set(placeholderText) { this.gObject.placeholderText = it }
    }
}