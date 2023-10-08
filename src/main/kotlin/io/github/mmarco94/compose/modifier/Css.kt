package io.github.mmarco94.compose.modifier

fun Modifier.cssClasses(vararg classes: String) = combine(
    apply = { it.cssClasses = classes },
    undo = { it.cssClasses = emptyArray() }
)

fun Modifier.cssClasses(classes: List<String>) = combine(
    apply = { it.cssClasses = classes.toTypedArray() },
    undo = { it.cssClasses = emptyArray() }
)