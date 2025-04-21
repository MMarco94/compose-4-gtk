import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.*
import io.github.mmarco94.compose.gtk.components.Box
import io.github.mmarco94.compose.gtk.components.Button
import io.github.mmarco94.compose.gtk.components.Label
import io.github.mmarco94.compose.gtk.components.ToggleButton
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.margin
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.HelloApp", args) {
        ApplicationWindow("Test", onClose = ::exitApplication) {
            val pageState = remember { CarouselState(8) }
            val orientation = remember { mutableStateOf(Orientation.HORIZONTAL) }
            val allowLongSwipes = remember { mutableStateOf(false) }
            val allowMouseDrag = remember { mutableStateOf(true) }
            val allowScrollWheel = remember { mutableStateOf(true) }
            val animateScrollTo = remember { mutableStateOf(true) }

            Box(orientation = Orientation.VERTICAL) {
                HeaderBar(title = { Label("Current page: ${pageState.currentPage}") })

                Carousel(
                    state = pageState,
                    orientation = orientation.value,
                    allowLongSwipes = allowLongSwipes.value,
                    allowMouseDrag = allowMouseDrag.value,
                    allowScrollWheel = allowScrollWheel.value,
                    spacing = 80,
                    animateScrollTo = animateScrollTo.value,
                    onPageChanged = { index ->
                        println("Page changed to $index")
                        println("State current page changed to ${pageState.currentPage}")
                    },
                ) { page ->
                    when (page) {
                        0 -> Presentation()
                        1 -> Settings(allowLongSwipes, allowMouseDrag, allowScrollWheel)
                        2 -> MoreSettings(orientation)
                        else -> {
                            StatusPage(title = "Page $page") {
                                Label("Status page $page")
                            }
                        }
                    }
                }

                Box(orientation = Orientation.VERTICAL, modifier = Modifier.margin(16), spacing = 16) {
                    ToggleButton("Animate scroll to", active = animateScrollTo.value) {
                        animateScrollTo.value = !animateScrollTo.value
                    }
                    Button("Scroll to page 4") {
                        pageState.currentPage = 4
                    }
                }
            }
        }
    }
}

@Composable
fun Presentation() {
    StatusPage(title = "Carousel") {
        Label("This is a carousel component")
    }
}

@Composable
fun Settings(
    allowLongSwipes: MutableState<Boolean>,
    allowMouseDrag: MutableState<Boolean>,
    allowScrollWheel: MutableState<Boolean>
) {
    StatusPage(title = "Settings") {
        Box(orientation = Orientation.VERTICAL, modifier = Modifier.margin(16), spacing = 16) {
            Label("It can be modified")
            ToggleButton(
                label = "Allow long swipes",
                active = allowLongSwipes.value,
            ) {
                allowLongSwipes.value = !allowLongSwipes.value
            }
            ToggleButton(
                label = "Allow mouse drag",
                active = allowMouseDrag.value,
            ) {
                allowMouseDrag.value = !allowMouseDrag.value
            }
            ToggleButton(
                label = "Allow scroll wheel",
                active = allowScrollWheel.value,
            ) {
                allowScrollWheel.value = !allowScrollWheel.value
            }
        }
    }
}

@Composable
fun MoreSettings(orientation: MutableState<Orientation>) {
    StatusPage(title = "More Settings!") {
        Box(orientation = Orientation.VERTICAL, modifier = Modifier.margin(16), spacing = 16) {
            Label("I am ${if (orientation.value == Orientation.HORIZONTAL) "Horizontal" else "Vertical"}")
            Button(label = "Change orientation") {
                if (orientation.value == Orientation.HORIZONTAL) {
                    orientation.value = Orientation.VERTICAL
                } else {
                    orientation.value = Orientation.HORIZONTAL
                }
            }
        }
    }
}