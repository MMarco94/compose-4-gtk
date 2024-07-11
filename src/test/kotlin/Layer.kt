package kbar;

import androidx.compose.runtime.*
import com.sun.security.auth.module.UnixSystem
import io.github.jwharm.javagi.base.Out
import io.github.mmarco94.compose.ApplicationScope
import io.github.mmarco94.compose.GtkLayerShell
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.*
import kbar.HyprlandIpc.Events
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gnome.gio.UnixSocketAddress
import org.gnome.gtk.Align
import org.gnome.gtk.Application
import org.gnome.gtk.ContentFit
import org.gnome.gtk.IconLookupFlags
import org.gnome.gtk.IconPaintable
import org.gnome.gtk.IconTheme
import org.gnome.gtk.RevealerTransitionType
import org.gnome.gtk.TextDirection
import java.net.StandardProtocolFamily
import java.net.UnixDomainSocketAddress
import java.nio.channels.Channels
import java.nio.channels.SocketChannel
import java.nio.file.Path
import java.util.Calendar
import kotlin.concurrent.timer
import io.github.mmarco94.compose.gtk.application as gtkApplication
import io.github.mmarco94.compose.gtk.components.ApplicationWindow as GtkApplicationWindow

val Application.iconTheme: IconTheme by lazy {
    IconTheme.builder()
        .setThemeName("Win11-blue-dark")
        .build()
}

fun main(args: Array<String>) {
    GtkLayerShell.preload()
    gtkApplication("my.example.HelloApp", args) {
        GtkApplicationWindow(
            application,
            title = "Test",
            defaultHeight = 50,
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
            CenterBox(
                start = {
                    TitleBar()
                },
                end = {
                    HorizontalBox(spacing = 8) {
                        Time()
                        PowerMenu()
                    }
                },
            ) {
                Workspaces()
            }
        }
    }
}

@Composable
private fun Time() {
    Box {
        val getTime: () -> String = getTime@{
            val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val format = currentTime.let {
                val minute = if (it.minute < 10) "0${it.minute}" else it.minute
                val second = if (it.second < 10) "0${it.second}" else it.second
                val day = if (it.dayOfMonth < 10) "0${it.dayOfMonth}" else it.dayOfMonth
                val month = if (it.monthNumber < 10) "0${it.monthNumber}" else it.monthNumber
                "${it.hour}:${minute}:${second} $day.${month}.${it.year}"
            }
            return@getTime format
        }
        var time by remember { mutableStateOf(getTime()) }
        DisposableEffect(true) {
            val timer = timer(startAt = Calendar.getInstance().time, daemon = true, period = 500) {
                time = getTime()
            }
            return@DisposableEffect object : DisposableEffectResult {
                override fun dispose() {
                    timer.cancel()
                }
            }
        }
        Label(time)
    }
}

@Composable
private fun ApplicationScope.TitleBar() {
    var title by remember { mutableStateOf("Hello World") }
    var icon: IconPaintable? by remember { mutableStateOf(null) }
    LaunchedEffect(true) {
        launch(Dispatchers.IO) {
            HyprlandIpc.start()
            HyprlandIpc.addListener(Events.WINDOW_ACTIVE) {
                val iconName = it.first()
                icon = if (application.iconTheme.hasIcon(iconName)) {
                    application.iconTheme.lookupIcon(
                        iconName,
                        emptyArray(),
                        24,
                        2,
                        TextDirection.NONE,
                        IconLookupFlags.FORCE_REGULAR
                    )
                } else {
                    null
                }
                title = it.last()
            }
        }
    }
    HorizontalBox(modifier = Modifier.horizontalAlignment(Align.START)) {
        if (icon != null) {
            Picture(image = icon!!, contentFit = ContentFit.SCALE_DOWN)
        }
        if (title.isNotBlank()) {
            Label(title)
        }
    }
}

@Composable
private fun ApplicationScope.PowerMenu() {
    var reveal by remember { mutableStateOf(false) }
    HorizontalBox(modifier = Modifier.horizontalAlignment(Align.END)
        .hover({ reveal = false }) { _, _ -> reveal = true }) {
        SymbolicIcon("system-shutdown-symbolic", application.iconTheme, size = 28)
        Revealer(
            reveal,
            transitionType = RevealerTransitionType.SLIDE_RIGHT,
            transitionDuration = 200
        ) {
            HorizontalBox(modifier = Modifier.horizontalAlignment(Align.END)) {
                Button("", child = {
                    SymbolicIcon("system-shutdown-symbolic", application.iconTheme, size = 28)
                }) {
                    exec("systemctl shutdown")
                }
                Button("", child = {
                    SymbolicIcon("system-reboot-symbolic", application.iconTheme)
                }) {
                    exec("systemctl reboot")
                }
                Button("", child = {
                    SymbolicIcon("system-hibernate-symbolic", application.iconTheme)
                }) {
                    exec("hyprctl dispatch dpms off")
                }
                Button("", child = {
                    SymbolicIcon("system-lock-screen-symbolic", application.iconTheme)
                }) {
                    exec("hyprlock --immediate")
                }
            }
        }
    }
}

@Composable
fun SymbolicIcon(
    name: String,
    iconTheme: IconTheme,
    size: Int = 24,
    scale: Int = 2,
    modifier: Modifier = Modifier,
) {
    Picture(
        image = iconTheme.lookupIcon(
            name,
            emptyArray(),
            size,
            scale,
            TextDirection.NONE,
            setOf()
        ), contentFit = ContentFit.SCALE_DOWN,
        modifier = modifier
    )
}

@Composable
fun Workspaces(

) {
    HorizontalBox(
        modifier = Modifier.horizontalAlignment(Align.CENTER).click(true) { -> println("Something") },
        spacing = 8,
        homogeneous = true
    ) {
        val workspaces = remember { mutableStateListOf<HyprlandIpc.Workspace>() }
        var workspace by remember {
            mutableStateOf(1)
        }
        LaunchedEffect(true) {
            launch(Dispatchers.IO) {
                val getWorkspaces = suspend {
                    val ws = HyprlandIpc.message("j/workspaces")
                    println(ws)
                    val workspaceList = Json.decodeFromString<List<HyprlandIpc.Workspace>>(ws)
                    workspaces.clear()
                    workspaces.addAll(workspaceList.sortedBy { it.id })
                }
                try {
                    getWorkspaces()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                launch(Dispatchers.IO) {
                    HyprlandIpc.addListener(Events.WORKSPACE_CHANGED_V2) {
                        val wsNum = it.first().toInt()
                        if (!workspaces.any { it.id == wsNum }) {
                            getWorkspaces()
                        }
                        workspace = wsNum
                    }
                }
                launch(Dispatchers.IO) {
                    HyprlandIpc.addListener(Events.MONITOR_FOCUSED) {
                        workspace = it.last().toInt()
                    }
                }
                launch(Dispatchers.IO) {
                    HyprlandIpc.addListener(Events.WORKSPACE_DESTROYED) { params ->
                        val wsNum = params.first().toInt()
                        if (workspaces.any { it.id == wsNum }) {
                            getWorkspaces()
                        }
                    }
                }
            }
        }
        workspaces.forEach {
            Button(
                it.name,
                modifier = Modifier.cssClasses("workspace", it.name, if (workspace == it.id) "selected" else "")
                    .sizeRequest(32)
            ) {
                runBlocking {
                    HyprlandIpc.message("dispatch workspace ${it.id}")
                }
            }
        }
    }
}

fun exec(command: String) {
    Runtime.getRuntime().exec(arrayOf("bash", "-c", command))
}

object HyprlandIpc {
    lateinit var eventFlow: Flow<Action?>
    var isStarted = false
    private val eventSocket: UnixDomainSocketAddress
    private val callSocket: Path

    init {
        val hyprlandSignature = System.getenv("HYPRLAND_INSTANCE_SIGNATURE")
            .ifEmpty {
                throw IllegalArgumentException("Missing HYPRLAND_INSTANCE_SIGNATURE. Is Hyprland running?")
            }
        val xdgRuntimeDir = System.getenv("XDG_RUNTIME_DIR")
            .ifEmpty {
                "/run/user/${UnixSystem().uid}"
            }

        val socketPath = Path.of(xdgRuntimeDir)
            .resolve("hypr")
            .resolve(hyprlandSignature)

        callSocket = socketPath.resolve(".socket.sock")
        eventSocket = UnixDomainSocketAddress.of(socketPath.resolve(".socket2.sock"))
    }


    suspend fun start() = withContext(Dispatchers.IO) {
        if (isStarted) {
            return@withContext
        }

        eventFlow = flow {
            val socketChannel = SocketChannel.open(StandardProtocolFamily.UNIX)
            socketChannel.connect(eventSocket)
            Channels.newInputStream(socketChannel).bufferedReader().use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    line?.split("\n")?.forEach {
                        if (it.isEmpty()) {
                            return@forEach
                        }
                        val action = it.split(">>")
                        val event = action[0]
                        val parameters = if (action.size == 2) {
                            action[1].split(",")
                        } else {
                            emptyList()
                        }
                        emit(
                            Action(
                                Events.fromValue(event),
                                parameters
                            )
                        )
                    }
                }
            }
        }
            .flowOn(Dispatchers.IO)

        isStarted = true
    }

    suspend fun socketStream(command: String) = withContext(Dispatchers.IO) {
        val socketClient = org.gnome.gio.SocketClient.builder().build()
        val connection = socketClient.connect(
            UnixSocketAddress(callSocket.toString()),
            null
        )
        connection.getOutputStream().write(command.toByteArray(), null)

        val stream = org.gnome.gio.DataInputStream.builder()
            .setCloseBaseStream(true)
            .setBaseStream(connection.inputStream)
            .build()

        return@withContext connection to stream
    }

    suspend fun message(command: String) = withContext(Dispatchers.IO) {
        val (connection, stream) = socketStream(command)
        try {
            val response = stream.readUpto(Char(4).toString(), -1, Out(), null)
            println(response)
            return@withContext response
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            connection.close(null)
        }
        return@withContext ""
    }

    suspend fun addListener(
        event: Events,
        handler: suspend (parameters: List<String>) -> Unit,
    ) = withContext(Dispatchers.IO) {
        eventFlow.flowOn(Dispatchers.IO)
            .filterNotNull()
            .filter { it.event == event }
            .map { it.parameters }
            .collectLatest(handler)
    }

    @Serializable
    data class Workspace(
        val id: Int,
        val name: String,
        val monitor: String,
        @SerialName("monitorID")
        val monitorId: Int,
        val windows: Int,
        @SerialName("hasfullscreen")
        val hasFullscreen: Boolean,
        @SerialName("lastwindow")
        val lastWindow: String,
        @SerialName("lastwindowtitle")
        val lastWindowTitle: String,
    )

    data class Action(
        val event: Events,
        val parameters: List<String>,
    )


    enum class Events(
        val value: String,
    ) {
        WORKSPACE_CHANGED("workspace"),
        WORKSPACE_CHANGED_V2("workspacev2"),
        WORKSPACE_CREATED("createworkspace"),
        WORKSPACE_CREATED_V2("createworkspacev2"),
        WORKSPACE_DESTROYED("destroyworkspace"),
        WORKSPACE_DESTROYED_V2("destroyworkspacev2"),
        WORKSPACE_MOVED("moveworkspace"),
        WORKSPACE_MOVED_V2("moveworkspacev2"),
        WORKSPACE_RENAMED("renameworkspace"),
        ACTIVE_SPECIAL("activespecial"),
        LAYOUT_CHANGE("activelayout"),
        WINDOW_OPENED("openwindow"),
        WINDOW_CLOSED("closewindow"),
        WINDOW_MOVED("movewindow"),
        WINDOW_MOVED_V2("movewindowv2"),
        WINDOW_FLOATING("changefloatingmode"),
        WINDOW_URGENT("urgent"),
        WINDOW_MINIMIZED("minimize"),
        WINDOW_ACTIVE("activewindow"),
        WINDOW_ACTIVE_V2("activewindowv2"),
        WINDOW_TITLE_CHANGED("windowtitle"),
        WINDOW_PINNED("pin"),
        SCREENCAST("screencast"),
        LAYER_OPENED("openlayer"),
        LAYER_CLOSED("closelayer"),
        SUBMAP("submap"),
        FULLSCREEN("fullscreen"),
        MONITOR_FOCUSED("focusedmon"),
        MONITOR_REMOVED("monitorremoved"),
        MONITOR_ADDED("monitoradded"),
        MONITOR_ADDED_V2("monitoraddedv2"),
        GROUP_TOGGLED("togglegroup"),
        GROUP_MOVED_INTO("moveintogroup"),
        GROUP_MOVED_OUT_OF("moveoutofgroup"),
        GROUP_IGNORE_LOCK("ignoregrouplock"),
        GROUP_LOCKGROUPS("lockgroups"),
        CONFIG_RELOADED("configreloaded");

        companion object {
            fun fromValue(value: String): Events = entries.first { it.value == value }
        }
    }
}
