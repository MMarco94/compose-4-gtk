package io.github.mmarco94.compose.utils

fun <T> MutableList<T>.inPlaceMap(f: (T) -> T) {
    forEachIndexed { index, t ->
        this[index] = f(t)
    }
}