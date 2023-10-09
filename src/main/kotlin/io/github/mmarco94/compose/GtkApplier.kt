package io.github.mmarco94.compose

import androidx.compose.runtime.AbstractApplier
import org.gnome.gobject.GObject

internal class GtkApplier(root: GtkComposeNode<GObject>) : AbstractApplier<GtkComposeNode<GObject>>(root) {
    override fun insertBottomUp(index: Int, instance: GtkComposeNode<GObject>) = Unit
    override fun insertTopDown(index: Int, instance: GtkComposeNode<GObject>) {
        current.add(index, instance)
    }

    override fun remove(index: Int, count: Int) {
        repeat(count) {
            current.remove(index)
        }
    }

    override fun move(from: Int, to: Int, count: Int) {
        TODO("Move not yet implemented")
    }

    override fun onClear() {
        current.clear()
    }
}