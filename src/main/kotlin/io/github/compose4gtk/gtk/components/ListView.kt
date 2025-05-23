package io.github.compose4gtk.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import io.github.compose4gtk.GtkApplier
import io.github.compose4gtk.GtkComposeNode
import io.github.compose4gtk.GtkComposeWidget
import io.github.compose4gtk.LeafComposeNode
import io.github.compose4gtk.modifier.Modifier
import io.github.jwharm.javagi.gio.ListIndexModel
import org.gnome.gio.ListModel
import org.gnome.gio.ListStore
import org.gnome.gobject.GObject
import org.gnome.gtk.ListItem
import org.gnome.gtk.MultiSelection
import org.gnome.gtk.NoSelection
import org.gnome.gtk.SelectionModel
import org.gnome.gtk.SignalListItemFactory
import org.gnome.gtk.SingleSelection
import org.gnome.gtk.ListView as GTKListView

/**
 * The [GtkComposeNode] for each item of a [org.gnome.gtk.ListView].
 */
private class GtkListItemComposeNode(val listItem: ListItem) : GtkComposeNode {
    override fun addNode(index: Int, child: GtkComposeNode) {
        require(index == 0) {
            "ListItem support a single child"
        }
        require(listItem.child == null) {
            "ListItem support a single child"
        }
        require(child is GtkComposeWidget<*>) {
            "ListItem support only GTK widgets"
        }
        listItem.child = child.widget
    }

    override fun removeNode(index: Int) {
        require(index == 0) {
            "ListItem support a single child"
        }
        listItem.child = null
    }

    override fun clearNodes() {
        listItem.child = null
    }
}

/**
 * Creates a [org.gnome.gtk.ListView] with [items] items.
 * Each element is a composable created using [child].
 *
 * The created [org.gnome.gio.ListModel] will have no selection mode.
 * You can use `ListView(model){ ... }` if you want more customization options.
 */
@Composable
fun ListView(
    items: Int,
    modifier: Modifier = Modifier,
    child: @Composable (index: Int) -> Unit,
) {
    val model = remember {
        ListStore<ListIndexModel.ListIndex>()
    }
    remember(items) {
        while (model.size > items) {
            model.removeLast()
        }
        while (model.size < items) {
            model.append(ListIndexModel.ListIndex(model.size))
        }
    }
    val selectionModel = remember(model) {
        NoSelection<ListIndexModel.ListIndex>(model)
    }
    ListView(selectionModel, modifier) {
        child(it.index)
    }
}

/**
 * Creates a [org.gnome.gtk.ListView] bound to the given [model].
 * Each element is a composable created using [child].
 */
@Composable
fun <T : GObject> ListView(
    model: SelectionModel<T>,
    modifier: Modifier = Modifier,
    child: @Composable (item: T) -> Unit,
) {
    val compositionContext = rememberCompositionContext()

    ComposeNode<GtkComposeWidget<GTKListView>, GtkApplier>(
        factory = {
            LeafComposeNode(
                widget = GTKListView.builder()
                    .setFactory(createListItemFactory(compositionContext, child))
                    .build(),
            )
        },
        update = {
            set(modifier) { applyModifier(it) }
            set(model) { this.widget.model = it }
        },
    )
}

@Composable
fun <Item : GObject> rememberNoSelectionModel(
    vararg keys: Any,
    itemsFactory: () -> List<Item>,
): SelectionModel<Item> =
    rememberSelectionModel(
        keys = keys,
        itemsFactory = itemsFactory,
        selectionModelFactory = { model -> NoSelection(model) },
    )

@Composable
fun <Item : GObject> rememberSingleSelectionModel(
    vararg keys: Any,
    itemsFactory: () -> List<Item>,
): SelectionModel<Item> =
    rememberSelectionModel(
        keys = keys,
        itemsFactory = itemsFactory,
        selectionModelFactory = { model -> SingleSelection(model) },
    )

@Composable
fun <Item : GObject> rememberMultiSelectionModel(
    vararg keys: Any,
    itemsFactory: () -> List<Item>,
): SelectionModel<Item> =
    rememberSelectionModel(
        keys = keys,
        itemsFactory = itemsFactory,
        selectionModelFactory = { model -> MultiSelection(model) },
    )

@Composable
private fun <Item : GObject, Model : SelectionModel<Item>> rememberSelectionModel(
    vararg keys: Any,
    itemsFactory: () -> List<Item>,
    selectionModelFactory: (model: ListModel<Item>) -> Model,
): SelectionModel<Item> {
    val model = remember { ListStore<Item>() }

    remember(*keys) {
        val items = itemsFactory()

        while (model.size > items.size) {
            model.removeLast()
        }

        for (i in 0 until model.size) {
            model[i] = items[i]
        }

        while (model.size < items.size) {
            model.append(items[model.size])
        }
    }

    return remember { selectionModelFactory(model) }
}

/**
 * Creates a [SignalListItemFactory] where each element is provided by [child].
 */
private fun <T : GObject> createListItemFactory(
    compositionContext: CompositionContext,
    child: @Composable (T) -> Unit,
): SignalListItemFactory {
    val compositionMap = HashMap<ListItem, Composition>()
    val factory = SignalListItemFactory.builder().build()
    factory.onSetup { listItem ->
        listItem as ListItem
        compositionMap[listItem] = Composition(
            GtkApplier(GtkListItemComposeNode(listItem)),
            compositionContext,
        )
    }
    factory.onBind { listItem ->
        listItem as ListItem
        val item = listItem.item as T
        val composition = compositionMap[listItem]
        checkNotNull(composition)
        composition.setContent {
            child(item)
        }
    }
    factory.onTeardown { listItem ->
        listItem as ListItem
        val composition = compositionMap.remove(listItem)
        checkNotNull(composition)
        composition.dispose()
    }
    return factory
}
