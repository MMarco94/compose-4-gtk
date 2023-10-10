package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.VirtualComposeNode
import io.github.mmarco94.compose.VirtualComposeNodeContainer
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.Flap
import org.gnome.adw.FlapFoldPolicy
import org.gnome.adw.FlapTransitionType
import org.gnome.adw.FoldThresholdPolicy
import org.gnome.gtk.Orientation
import org.gnome.gtk.PackType
import org.gnome.gtk.Widget
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


/**
 * TODO:
 *  - reveal flap
 *  - reveal params
 */
@Composable
fun Flap(
    flap: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    flapPosition: PackType = PackType.START,
    foldDuration: Duration = 250.milliseconds,
    foldPolicy: FlapFoldPolicy = FlapFoldPolicy.AUTO,
    foldThresholdPolicy: FoldThresholdPolicy = FoldThresholdPolicy.MINIMUM,
    locked: Boolean = false,
    modal: Boolean = true,
    separator: @Composable () -> Unit = {},
    swipeToClose: Boolean = true,
    swipeToOpen: Boolean = true,
    transitionType: FlapTransitionType = FlapTransitionType.OVER,
    orientation: Orientation = Orientation.HORIZONTAL,
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Flap>, GtkApplier>(
        factory = {
            VirtualComposeNodeContainer(Flap.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(flapPosition) { this.gObject.flapPosition = it }
            set(foldDuration) { this.gObject.foldDuration = it.inWholeMilliseconds.toInt() }
            set(foldPolicy) { this.gObject.foldPolicy = it }
            set(foldThresholdPolicy) { this.gObject.foldThresholdPolicy = it }
            set(locked) { this.gObject.locked = it }
            set(modal) { this.gObject.modal = it }
            set(swipeToClose) { this.gObject.swipeToClose = it }
            set(swipeToOpen) { this.gObject.swipeToOpen = it }
            set(transitionType) { this.gObject.transitionType = it }
            set(orientation) { this.gObject.orientation = it }
        },
        content = {
            FlapContent {
                flap()
            }
            Separator {
                separator()
            }
            Content {
                content()
            }
        },
    )
}

@Composable
private fun Content(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<Flap> { overlay ->
                SingleChildComposeNode(
                    overlay,
                    set = { setContent(it) },
                )
            }
        },
        update = { },
        content = content,
    )
}

@Composable
private fun Separator(
    separator: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<Flap> { overlay ->
                SingleChildComposeNode(
                    overlay,
                    set = { setSeparator(it) },
                )
            }
        },
        update = { },
        content = separator,
    )
}

@Composable
private fun FlapContent(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<Flap> { overlay ->
                SingleChildComposeNode(
                    overlay,
                    set = { flap = it },
                )
            }
        },
        update = { },
        content = content,
    )
}
