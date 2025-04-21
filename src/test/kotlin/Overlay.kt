import androidx.compose.runtime.getValue
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.adw.components.OverlaySplitView
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.expandVertically
import io.github.mmarco94.compose.modifier.margin
import org.gnome.adw.BreakpointCondition
import org.gnome.gtk.PolicyType

private val BREAKPOINT_CONDITION = BreakpointCondition.parse("min-width: 800px")

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(
            "Overlay",
            onClose = ::exitApplication,
            defaultWidth = 800,
            defaultHeight = 640,
        ) {
            val isBig by rememberBreakpoint(BREAKPOINT_CONDITION)
            OverlaySplitView(
                sidebar = {
                    VerticalBox {
                        HeaderBar(
                            title = { Label("Sidebar") }
                        )
                    }
                },
                collapsed = !isBig,
            ) {
                VerticalBox {
                    HeaderBar(
                        title = { Label("Content") },
                        startWidgets = {
                            Button(if (showSidebar) "Hide sidebar" else "Show sidebar") {
                                if (showSidebar) hideSidebar()
                                else showSidebar()
                            }
                        },
                    )
                    ScrolledWindow(
                        horizontalScrollbarPolicy = PolicyType.NEVER,
                        modifier = Modifier.expandVertically()
                    ) {
                        VerticalBox(Modifier.margin(40), spacing = 40) {
                            repeat(10) { index ->
                                Frame {
                                    Label("Hello World#$index", Modifier.margin(10))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
