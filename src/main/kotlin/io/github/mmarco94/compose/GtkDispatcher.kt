package io.github.mmarco94.compose

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.gnome.glib.GLib
import kotlin.coroutines.CoroutineContext

// TODO: implement Delay
class GtkDispatcher : CoroutineDispatcher() {
    var active = false
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (active) {
            GLib.idleAddOnce {
                block.run()
            }
        } else {
            block.run()
        }
    }
}