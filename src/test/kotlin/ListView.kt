import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.compose4gtk.adw.application
import io.github.compose4gtk.adw.components.ApplicationWindow
import io.github.compose4gtk.adw.components.HeaderBar
import io.github.compose4gtk.gtk.components.Button
import io.github.compose4gtk.gtk.components.HorizontalBox
import io.github.compose4gtk.gtk.components.Label
import io.github.compose4gtk.gtk.components.ListView
import io.github.compose4gtk.gtk.components.ScrolledWindow
import io.github.compose4gtk.gtk.components.VerticalBox
import io.github.compose4gtk.gtk.components.rememberMultiSelectionModel
import io.github.compose4gtk.modifier.Modifier
import io.github.compose4gtk.modifier.expand
import org.gnome.gobject.GObject

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow("Test", onClose = ::exitApplication, defaultWidth = 800, defaultHeight = 800) {
            VerticalBox {
                HeaderBar(title = { Label("ListView") })
                var itemVersion by remember { mutableStateOf(0) }
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
                            val model = rememberMultiSelectionModel(itemVersion, itemSize) {
                                List(itemSize) { index ->
                                    CustomItem("Custom item version $itemVersion #${index}")
                                }
                            }
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
                    if (itemSize > 0) {
                        itemSize -= 1
                    }
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
                Button("Increase item version") {
                    itemVersion++
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
private fun Panel(title: String, content: @Composable () -> Unit) {
    VerticalBox(Modifier.expand()) {
        HeaderBar(title = { Label(title) }, showEndTitleButtons = false, showStartTitleButtons = false)
        ScrolledWindow(Modifier.expand()) {
            content()
        }
    }
}
