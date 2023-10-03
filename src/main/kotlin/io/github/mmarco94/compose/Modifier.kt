package io.github.mmarco94.compose

import org.gnome.gtk.Widget

interface Modifier {
    fun apply(w: Widget)
    fun undo(w: Widget)

    companion object : Modifier {
        override fun apply(w: Widget) = Unit
        override fun undo(w: Widget) = Unit

        fun of(
            apply: (Widget) -> Unit,
            undo: (Widget) -> Unit,
        ) = object : Modifier {
            override fun apply(w: Widget) = apply(w)
            override fun undo(w: Widget) = undo(w)
        }
    }
}


operator fun Modifier.plus(another: Modifier): Modifier {
    val me = this
    return Modifier.of(
        apply = {
            me.apply(it)
            another.apply(it)
        },
        undo = {
            another.undo(it)
            me.undo(it)
        }
    )
}

fun Modifier.margin(margin: Int) = margin(margin, margin)
fun Modifier.margin(horizontal: Int, vertical: Int) = margin(horizontal, vertical, horizontal, vertical)
fun Modifier.margin(
    start: Int,
    top: Int,
    end: Int,
    bottom: Int,
): Modifier {
    return Modifier.of(
        apply = {
            it.marginStart = start
            it.marginTop = top
            it.marginEnd = end
            it.marginBottom = bottom
        },
        undo = {
            it.marginStart = 0
            it.marginTop = 0
            it.marginEnd = 0
            it.marginBottom = 0
        }
    )
}
