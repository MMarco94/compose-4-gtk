package io.github.mmarco94.compose

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.gnome.gobject.GObject
import org.gnome.gtk.Application
import org.gnome.gtk.Window
import kotlin.system.exitProcess

private class GtkApplicationComposeNode(gObject: Application) : GtkContainerComposeNode<Application, Window>(gObject) {
    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        val childWindows = child.gObject
        if (childWindows !is Window) {
            throw UnsupportedOperationException()
        }
        childWindows.present()
        super.add(index, child)
    }

    override fun remove(index: Int) {
        children[index].destroy()
        super.remove(index)
    }

    override fun clear() {
        children.forEach { it.destroy() }
        super.clear()
    }
}

@Stable
interface ApplicationScope {
    val application: Application
    fun exitApplication()
}

val LocalApplication = staticCompositionLocalOf<Application> { throw RuntimeException("not in a GTK application") }

fun Application.initializeApplication(
    args: Array<String>,
    content: @Composable ApplicationScope.() -> Unit,
) {
    val app = this
    val dispatcher = GtkDispatcher
    runBlocking(dispatcher) {
        withContext(YieldFrameClock) {
            startSnapshotManager()

            val recomposer = Recomposer(coroutineContext)
            var isOpen by mutableStateOf(true)

            val appScope = object : ApplicationScope {
                override val application = app
                override fun exitApplication() {
                    isOpen = false
                }
            }

            launch {
                recomposer.runRecomposeAndApplyChanges()
            }

            val composition = Composition(GtkApplier(GtkApplicationComposeNode(app)), recomposer)
            app.onActivate {
                GtkDispatcher.active = true
                composition.setContent {
                    if (isOpen) {
                        CompositionLocalProvider(LocalApplication provides app) {
                            appScope.content()
                        }
                    }
                }
            }
            val status = app.run(args)
            GtkDispatcher.active = false
            try {
                recomposer.close()
                recomposer.join()
            } finally {
                composition.dispose()
            }
            exitProcess(status)
        }
    }
}

private fun CoroutineScope.startSnapshotManager() {
    val channel = Channel<Unit>(Channel.CONFLATED)
    launch {
        channel.consumeEach {
            Snapshot.sendApplyNotifications()
        }
    }
    Snapshot.registerGlobalWriteObserver {
        channel.trySend(Unit)
    }
}

private object YieldFrameClock : MonotonicFrameClock {
    override suspend fun <R> withFrameNanos(
        onFrame: (frameTimeNanos: Long) -> R,
    ): R {
        yield()
        return onFrame(System.nanoTime())
    }
}