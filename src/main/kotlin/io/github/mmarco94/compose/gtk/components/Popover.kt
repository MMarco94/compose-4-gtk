package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gtk.Popover

interface PopoverScope {
    var gObject: Popover?
}

@Composable
fun Popover(
    modifier: Modifier = Modifier,
    arrow: Boolean = true,
    content: @Composable () -> Unit,
    trigger: @Composable PopoverScope.() -> Unit,
) {
    val scope = object : PopoverScope {
        override var gObject: Popover? = null
    }
    ComposeNode<GtkComposeWidget<Popover>, GtkApplier>(
        factory = {
            val popover = Popover.builder()
                .setHasArrow(arrow)
                .build()
            scope.gObject = popover
            SingleChildComposeNode<Popover>(
                popover
            ) {
                child = it
            }
        },
        update = {
            set(modifier) { applyModifier(it) }
        },
        content = content
    )
    scope.trigger()
}