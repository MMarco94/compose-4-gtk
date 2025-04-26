import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.mmarco94.compose.adw.application
import io.github.mmarco94.compose.adw.components.*
import io.github.mmarco94.compose.gtk.components.Box
import io.github.mmarco94.compose.gtk.components.Button
import io.github.mmarco94.compose.gtk.components.Label
import io.github.mmarco94.compose.gtk.components.ToggleButton
import io.github.mmarco94.compose.gtk.components.VerticalBox
import io.github.mmarco94.compose.modifier.Modifier
import io.github.mmarco94.compose.modifier.expand
import io.github.mmarco94.compose.modifier.margin
import org.gnome.gtk.Orientation

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow("Carousel", onClose = ::exitApplication) {
            var pageCount by remember { mutableStateOf(8) }
            val orientation = remember { mutableStateOf(Orientation.HORIZONTAL) }
            val carouselState = rememberCarouselState(pageCount, orientation.value)
            val allowLongSwipes = remember { mutableStateOf(false) }
            val allowMouseDrag = remember { mutableStateOf(true) }
            val allowScrollWheel = remember { mutableStateOf(true) }

            VerticalBox {
                HeaderBar(title = { Label("Current page: ${carouselState.currentPage}/${carouselState.pageCount}") })

                Box(
                    orientation = if (orientation.value == Orientation.HORIZONTAL) Orientation.VERTICAL else Orientation.HORIZONTAL,
                    modifier = Modifier.margin(8, 0)
                ) {
                    Carousel(
                        state = carouselState,
                        modifier = Modifier.expand(true),
                        allowLongSwipes = allowLongSwipes.value,
                        allowMouseDrag = allowMouseDrag.value,
                        allowScrollWheel = allowScrollWheel.value,
                        spacing = 80,
                        onPageChanged = { index ->
                            println("Page changed to $index")
                            println("State current page changed to ${carouselState.currentPage}")
                        },
                    ) { page ->
                        when (page) {
                            0 -> Presentation()
                            1 -> Settings(allowLongSwipes, allowMouseDrag, allowScrollWheel)
                            2 -> MoreSettings(orientation)
                            else -> {
                                StatusPage(title = "Page $page", description = "Status page $page") {
                                    VerticalBox {
                                        if (page == pageCount - 1) {
                                            Label("You reached the end!")
                                            Button("Add page") {
                                                pageCount += 1
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    CarouselIndicatorDots(carouselState)
                    CarouselIndicatorLines(carouselState)
                }

                Box(orientation = Orientation.VERTICAL, modifier = Modifier.margin(16), spacing = 16) {
                    Button("Snap scroll to page 4") {
                        carouselState.scrollTo(4, animate = false)
                    }
                    Button("Scroll to last") {
                        carouselState.scrollTo(pageCount - 1)
                    }
                    Button("Scroll to first") {
                        carouselState.scrollTo(0)
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
    allowScrollWheel: MutableState<Boolean>,
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