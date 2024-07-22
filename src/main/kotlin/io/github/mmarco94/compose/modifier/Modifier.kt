package io.github.mmarco94.compose.modifier

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

        fun of(
            apply: (Widget) -> Unit,
        ) = object : Modifier {
            override fun apply(w: Widget) = apply(w)
            override fun undo(w: Widget) = Unit
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

fun Modifier.combine(
    apply: (Widget) -> Unit,
    undo: (Widget) -> Unit,
) = this + Modifier.of(apply, undo)
