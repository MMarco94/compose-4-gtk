package io.github.mmarco94.compose

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import org.gnome.glib.GLib
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

@Suppress("UnusedReceiverParameter")
val Dispatchers.Gtk get() = GtkDispatcher

// TODO: implement Delay
object GtkDispatcher : CoroutineDispatcher() {
    internal var active = false
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