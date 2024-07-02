package kbar;

import io.github.mmarco94.compose.GtkLayerShell
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.gtk.application as gtkApplication
import io.github.mmarco94.compose.gtk.components.ApplicationWindow as GtkApplicationWindow
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.layerShell


fun main(args: Array<String>) {
    GtkLayerShell.preload()
    gtkApplication("my.example.HelloApp", args) {
        GtkApplicationWindow(
            application,
            title = "Test",
            defaultHeight = 64,
            onClose = ::exitApplication,
            modifier = Modifier.layerShell(
                layer = GtkLayerShell.Layer.TOP,
                margins = mapOf(
                    GtkLayerShell.Edge.TOP to 12,
                    GtkLayerShell.Edge.BOTTOM to 12,
                    GtkLayerShell.Edge.LEFT to 20,
                    GtkLayerShell.Edge.RIGHT to 20,
                ),
                anchors = listOf(
                    GtkLayerShell.Edge.TOP,
                    GtkLayerShell.Edge.LEFT,
                    GtkLayerShell.Edge.RIGHT
                ),
                autoExclusiveZone = true
            )
        ) {
            HeaderBar(
                startWidgets = { Label("Start")},
                endWidgets = { Label("End") },
            )
        }
    }
}
