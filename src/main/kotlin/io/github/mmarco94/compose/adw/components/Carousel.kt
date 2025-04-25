package io.github.mmarco94.compose.adw.components

import androidx.compose.runtime.*
import io.github.jwharm.javagi.gobject.SignalConnection
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeWidget
import io.github.mmarco94.compose.GtkContainerComposeNode
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.Carousel
import org.gnome.adw.CarouselIndicatorDots
import org.gnome.adw.CarouselIndicatorLines
import org.gnome.adw.SpringParams
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private class AdwCarouselComposeNode(gObject: Carousel) : GtkContainerComposeNode<Carousel>(gObject) {
    var state: CarouselState? = null
    var animateScrollTo: Boolean? = null
    var onPageChanged: SignalConnection<Carousel.PageChangedCallback>? = null

    override fun add(index: Int, child: GtkComposeWidget<Widget>) {
        when (index) {
            children.size -> widget.append(child.widget)
            0 -> widget.insertAfter(child.widget, null)
            else -> widget.insertAfter(child.widget, children[index - 1])
        }
        super.add(index, child)
    }

    override fun remove(index: Int) {
        val child = children[index]
        widget.remove(child)
        super.remove(index)
    }

    override fun clear() {
        children.forEach { widget.remove(it) }
        super.clear()
    }
}

class CarouselState(val pageCount: Int) {
    var carousel: Carousel? = null
    var currentPage by mutableStateOf(0)
        private set

    fun scrollTo(pageNumber: Int) {
        if (pageNumber in 0 until pageCount) {
            currentPage = pageNumber
        }
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
    animateScrollTo: Boolean = true,
    onPageChanged: ((Int) -> Unit)? = null,
    content: @Composable (page: Int) -> Unit,
) {
    ComposeNode<AdwCarouselComposeNode, GtkApplier>(
        factory = {
            val gObject = Carousel()
            state.carousel = gObject
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
            set(animateScrollTo) { this.animateScrollTo = animateScrollTo }

            val currentPage = state.currentPage
            update(currentPage) {
                if (widget.visible && widget.mapped) {
                    widget.scrollTo(widget.getNthPage(currentPage), animateScrollTo)
                }
            }

            set(onPageChanged) {
                this.onPageChanged?.disconnect()
                this.onPageChanged = this.widget.onPageChanged { index ->
                    state.scrollTo(index)
                    if (onPageChanged != null) {
                        onPageChanged(index)
                    }
                }
            }
        },
        content = {
            repeat(state.pageCount) { index ->
                content(index)
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
        set(carouselState) { this.widget.carousel = it.carousel }
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
        set(carouselState) { this.widget.carousel = it.carousel }
        set(modifier) { applyModifier(it) }
        set(orientation) { this.widget.orientation = it }
    }
}