package io.github.mmarco94.compose.modifier

fun Modifier.sensitive(sensitive: Boolean = true) = combine(
    apply = { it.sensitive = sensitive },
    undo = { it.sensitive = true }
)