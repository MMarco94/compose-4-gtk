package io.github.mmarco94.compose.modifier

import io.github.mmarco94.compose.GtkLayerShell
import io.github.mmarco94.compose.GtkLayerShell.enableAutoExclusiveZone
import io.github.mmarco94.compose.GtkLayerShell.initLayer
import io.github.mmarco94.compose.GtkLayerShell.setAnchor
import io.github.mmarco94.compose.GtkLayerShell.setKeyboardMode
import io.github.mmarco94.compose.GtkLayerShell.setLayer
import io.github.mmarco94.compose.GtkLayerShell.setMargin
import io.github.mmarco94.compose.GtkLayerShell.setNamespace
import org.gnome.gtk.Window

fun Modifier.layerShell(
    namespace: String? = null,
    layer: GtkLayerShell.Layer = GtkLayerShell.Layer.BACKGROUND,
    margins: Map<GtkLayerShell.Edge, Int> = emptyMap(),
    anchors: List<GtkLayerShell.Edge> = emptyList(),
    autoExclusiveZone: Boolean = false,
    keyboardMode: GtkLayerShell.KeyboardMode? = null,
) = combine(
    apply = {
        if (it !is Window) {
            throw IllegalArgumentException("Modifier.layerShell must be used only with org.gnome.gtk.Window but used with ${it.javaClass.simpleName}")
        }
        it.initLayer()
        it.setLayer(layer)
        for ((edge, value) in margins) {
            it.setMargin(edge, value)
        }
        for (edge in anchors) {
            it.setAnchor(edge, true)
        }
        if (autoExclusiveZone) {
            it.enableAutoExclusiveZone()
        }
        namespace?.let { ns -> it.setNamespace(ns) }
        keyboardMode?.let { km -> it.setKeyboardMode(km) }
    },
    undo = {
        // This modifier cannot be undone and requires window to be recreated without it
    }
)