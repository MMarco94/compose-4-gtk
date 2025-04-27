import androidx.compose.runtime.*
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.ApplicationWindow
import io.github.mmarco94.compose.adw.components.HeaderBar
import io.github.mmarco94.compose.gtk.components.*
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.expand
import org.gnome.gio.ListStore
import org.gnome.gobject.GObject
import org.gnome.gtk.MultiSelection
import org.gnome.gtk.SelectionModel

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow("Test", onClose = ::exitApplication, defaultWidth = 800, defaultHeight = 800) {
            VerticalBox {
                HeaderBar(title = { Label("ListView") })
                var itemSize by remember { mutableStateOf(5) }
                var show by remember { mutableStateOf(true) }
                HorizontalBox(Modifier.expand()) {
                    if (show) {
                        Panel("Default model (no selection)") {
                            ListView(items = itemSize) { index ->
                                Label("Item #$index")
                            }
                        }
                        Panel("Custom model (multiple selection)") {
                            val model = createModel(itemSize)
                            ListView(model) { customItem ->
                                Label(customItem.name)
                            }
                        }
                    }
                }
                Button("Add one") {
                    itemSize += 1
                }
                Button("Remove last") {
                    itemSize -= 1
                }
                Button("Double items") {
                    itemSize *= 2
                }
                Button("Halve items") {
                    itemSize /= 2
                }
                Button("Remove all items") {
                    itemSize = 0
                }
                Button(if(show)"hide" else "show") {
                    show = !show
                }
            }
        }
    }
}

private class CustomItem(val name: String) : GObject()

@Composable
private fun createModel(count: Int): SelectionModel<CustomItem> {
    val model = remember { ListStore<CustomItem>() }
    while (model.size > count) {
        model.removeLast()
    }
    while (model.size < count) {
        model.append(CustomItem("Custom item #${model.size}"))
    }
    return remember { MultiSelection(model) }
}

@Composable
private fun Panel(title: String, content: @Composable () -> Unit) {
    VerticalBox(Modifier.expand()) {
        HeaderBar(title = { Label(title) }, showEndTitleButtons = false, showStartTitleButtons = false)
        ScrolledWindow(Modifier.expand()) {
            content()
        }
    }
}
