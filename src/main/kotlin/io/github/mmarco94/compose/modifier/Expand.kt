package io.github.mmarco94.compose.modifier

fun Modifier.expandVertically(expand: Boolean = true) = combine(
    apply = { it.vexpand = expand },
    undo = { it.vexpand = false }
)

fun Modifier.expandHorizontally(expand: Boolean = true) = combine(
    apply = { it.hexpand = expand },
    undo = { it.hexpand = false }
)