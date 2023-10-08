package io.github.mmarco94.compose.modifier

import org.gnome.gtk.Align

fun Modifier.alignment(horizontal: Align = Align.FILL, vertical: Align = Align.FILL): Modifier {
    return horizontalAlignment(horizontal).verticalAlignment(vertical)
}

fun Modifier.verticalAlignment(alignment: Align) = combine(
    apply = { it.valign = alignment },
    undo = { it.valign = Align.FILL }
)

fun Modifier.horizontalAlignment(alignment: Align) = combine(
    apply = { it.halign = alignment },
    undo = { it.halign = Align.FILL }
)