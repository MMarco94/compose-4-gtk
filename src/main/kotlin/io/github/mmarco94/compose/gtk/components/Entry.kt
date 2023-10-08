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

private class GtkEntryComposeNode(
    gObject: Entry,
    val onDeleteSignalHandler: SignalConnection<Editable.DeleteText>,
    val onInsertSignalHandler: SignalConnection<Editable.InsertText>,
) : LeafComposeNode<Entry>(gObject)

private data class TentativeCursorPosition(
    val position: Int,
    val condition: (String) -> Boolean,
)

@Composable
fun Entry(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String? = null,
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
        set(placeholderText) { this.gObject.placeholderText = it }
    }
}