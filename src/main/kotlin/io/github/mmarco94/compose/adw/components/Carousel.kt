package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.GtkContainerComposeNode
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.gtk.components.Box
import io.github.mmarco94.compose.gtk.components.CenterBox
import io.github.mmarco94.compose.gtk.components.Frame
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.Carousel
import org.gnome.adw.CarouselIndicatorDots
import org.gnome.adw.CarouselIndicatorLines
import org.gnome.adw.SpringParams
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private class AdwCarouselComposeNode(gObject: Carousel) : GtkContainerComposeNode<Carousel>(gObject) {
    var state: CarouselState? = null
    var onPageChanged: SignalConnection<Carousel.PageChangedCallback>? = null

    override fun add(index: Int, child: GtkComposeWidget<Widget>) {
        println("Add child $index")
        when (index) {
            children.size -> widget.append(child.widget)
            0 -> widget.insertAfter(child.widget, null)
            else -> widget.insertAfter(child.widget, children[index - 1])
        }
        super.add(index, child)
    }

    override fun remove(index: Int) {
        println("Remove child $index")
        val child = children[index]
        widget.remove(child)
        super.remove(index)
    }

    override fun clear() {
        children.forEach { widget.remove(it) }
        super.clear()
    }
}

sealed interface CarouselState {
    val carousel: Carousel?
    val currentPage: Int
    val pageCount: Int
    fun scrollTo(pageNumber: Int, animate: Boolean = true)
}

@Composable
fun rememberCarouselState(pageCount: Int): CarouselState {
    val state = remember { CarouselStateImpl() }
    state.pageCount = pageCount
    return state
}

private class CarouselStateImpl : CarouselState {
    override var carousel: Carousel? = null
        set(value) {
            check(field == null) { "CarouselState can be associated to a single Carousel" }
            requireNotNull(value)
            field = value
        }
    override var pageCount by mutableStateOf(0)
    override var currentPage by mutableStateOf(0)

    override fun scrollTo(pageNumber: Int, animate: Boolean) {
        val c = carousel ?: return
        c.scrollTo(c.getNthPage(pageNumber), animate)
    }
}

@Composable
fun Carousel(
    state: CarouselState,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
    allowLongSwipes: Boolean = false,
    allowMouseDrag: Boolean = true,
    allowScrollWheel: Boolean = true,
    interactive: Boolean = true,
    revealDuration: Int = 0,
    scrollParams: SpringParams = SpringParams(1.0, 0.5, 500.0),
    spacing: Int = 0,
    onPageChanged: ((Int) -> Unit)? = null,
    content: @Composable (page: Int) -> Unit,
) {
    val stateImpl: CarouselStateImpl = when (state) {
        is CarouselStateImpl -> state
    }
    ComposeNode<AdwCarouselComposeNode, GtkApplier>(
        factory = {
            val gObject = Carousel()
            stateImpl.carousel = gObject
            AdwCarouselComposeNode(gObject)
        },
        update = {
            set(state) { this.state = state }
            set(modifier) { applyModifier(it) }
            set(orientation) { this.widget.orientation = it }
            set(allowLongSwipes) { this.widget.allowLongSwipes = it }
            set(allowMouseDrag) { this.widget.allowMouseDrag = it }
            set(allowScrollWheel) { this.widget.allowScrollWheel = it }
            set(interactive) { this.widget.interactive = it }
            set(revealDuration) { this.widget.revealDuration = it }
            set(scrollParams) { this.widget.scrollParams = it }
            set(spacing) { this.widget.spacing = it }
            set(onPageChanged) {
                this.onPageChanged?.disconnect()
                this.onPageChanged = this.widget.onPageChanged { index ->
                    stateImpl.currentPage = index
                    if (onPageChanged != null) {
                        onPageChanged(index)
                    }
                }
            }
        },
        content = {
            repeat(state.pageCount) { index ->
                // This is necessary to have a predictable number of pages, regardless to how many composable
                // per page the caller adds.
                // Moreover, it prevents undesired scrolling when swapping a composable in a page.
                CenterBox {
                    content(index)
                }
            }
        },
    )
}

@Composable
fun CarouselIndicatorDots(
    carouselState: CarouselState,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
) {
    ComposeNode<GtkComposeWidget<CarouselIndicatorDots>, GtkApplier>({
        LeafComposeNode(CarouselIndicatorDots.builder().build())
    }) {
        set(carouselState.carousel) { this.widget.carousel = it }
        set(modifier) { applyModifier(it) }
        set(orientation) { this.widget.orientation = it }
    }
}

@Composable
fun CarouselIndicatorLines(
    carouselState: CarouselState,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.HORIZONTAL,
) {
    ComposeNode<GtkComposeWidget<CarouselIndicatorLines>, GtkApplier>({
        LeafComposeNode(CarouselIndicatorLines.builder().build())
    }) {
        set(carouselState.carousel) { this.widget.carousel = it }
        set(modifier) { applyModifier(it) }
        set(orientation) { this.widget.orientation = it }
    }
}