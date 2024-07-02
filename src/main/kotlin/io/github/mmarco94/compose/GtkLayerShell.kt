package io.github.mmarco94.compose

import io.github.jwharm.javagi.base.GLibLogger
import io.github.jwharm.javagi.gobject.InstanceCache
import io.github.jwharm.javagi.interop.Interop
import org.gnome.gdk.Monitor
import org.gnome.gobject.GObject
import org.gnome.gtk.Window
import java.lang.foreign.Arena
import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import java.lang.invoke.MethodHandle


object GtkLayerShell {

    private lateinit var MethodHandlesInstance: MethodHandles

    /**
     * Should be called at the start of main
     * because libgtk4-layer-shell must be loaded before libwayland-client
     * but calling java-gi results in libgtk-4 loading libwayland-client.
     *
     * As a workaround, LD_PRELOAD can be used to load libgtk4-layer-shell first.
     */
    fun preload() {
        println("Initializing gtk_layer_shell.")
        Interop.loadLibrary("libgtk4-layer-shell.so")
        Interop.loadLibrary("libwayland-client.so")
        this.MethodHandlesInstance = MethodHandles()
        println("gtk_layer_shell v${majorVersion()}.${minorVersion()}.${minorVersion()} protocol v${protocolVersion()} is initialized.")
    }

    /**
     * @return the major version number of the GTK Layer Shell library
     */
    fun majorVersion(): Int {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_major_version.invokeExact() as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result
    }

    /**
     * @return the minor version number of the GTK Layer Shell library
     */
    fun minorVersion(): Int {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_minor_version.invokeExact() as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result
    }

    /**
     * @return the micro/patch version number of the GTK Layer Shell library
     */
    fun microVersion(): Int {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_micro_version.invokeExact() as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result
    }

    /**
     * May block for a Wayland roundtrip the first time it's called.
     *
     * @return version of the zwlr_layer_shell_v1 protocol supported by the
     * compositor or 0 if the protocol is not supported.
     */
    fun protocolVersion(): Int {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_protocol_version.invokeExact() as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result
    }

    /**
     * May block for a Wayland roundtrip the first time it's called.
     *
     * @return %TRUE if the platform is Wayland and Wayland compositor supports the
     * zwlr_layer_shell_v1 protocol.
     */
    fun isSupported(): Boolean {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_is_supported.invokeExact() as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result != 0
    }

    /**
     * This class contains signatures of gtk_layer_shell functions.
     * The reason for it being a class and not an object is native libraries loading order
     */
    class MethodHandles {

        /**
         * guint gtk_layer_get_major_version ();
         */
        val gtk_layer_get_major_version: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_major_version", FunctionDescriptor.of(ValueLayout.JAVA_INT)
        )

        /**
         * guint gtk_layer_get_minor_version ();
         */
        val gtk_layer_get_minor_version: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_minor_version", FunctionDescriptor.of(ValueLayout.JAVA_INT)
        )

        /**
         * guint gtk_layer_get_micro_version ();
         */
        val gtk_layer_get_micro_version: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_micro_version", FunctionDescriptor.of(ValueLayout.JAVA_INT)
        )

        /**
         * gboolean gtk_layer_is_supported ();
         */
        val gtk_layer_is_supported: MethodHandle = Interop.downcallHandle(
            "gtk_layer_is_supported", FunctionDescriptor.of(ValueLayout.JAVA_INT)
        )

        /**
         * guint gtk_layer_get_protocol_version ();
         */
        val gtk_layer_get_protocol_version: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_protocol_version", FunctionDescriptor.of(ValueLayout.JAVA_INT)
        )

        /**
         * void gtk_layer_init_for_window (GtkWindow *window);
         */
        val gtk_layer_init_for_window: MethodHandle = Interop.downcallHandle(
            "gtk_layer_init_for_window", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS
            ), false
        )

        /**
         * void gtk_layer_set_layer (GtkWindow *window, GtkLayerShellLayer layer);
         */
        val gtk_layer_set_layer: MethodHandle = Interop.downcallHandle(
            "gtk_layer_set_layer", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ), false
        )

        /**
         * GtkLayerShellLayer gtk_layer_get_layer (GtkWindow *window);
         */
        val gtk_layer_get_layer: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_layer", FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS
            ), false
        )

        /**
         * void gtk_layer_set_margin (GtkWindow *window, GtkLayerShellEdge edge, int margin_size);
         */
        val gtk_layer_set_margin: MethodHandle = Interop.downcallHandle(
            "gtk_layer_set_margin", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT
            ), false
        )

        /**
         * int gtk_layer_get_margin (GtkWindow *window, GtkLayerShellEdge edge);
         */
        val gtk_layer_get_margin: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_margin", FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ), false
        )

        /**
         * void gtk_layer_set_anchor (GtkWindow *window, GtkLayerShellEdge edge, gboolean anchor_to_edge);
         */
        val gtk_layer_set_anchor: MethodHandle = Interop.downcallHandle(
            "gtk_layer_set_anchor", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT
            ), false
        )

        /**
         * gboolean gtk_layer_get_anchor (GtkWindow *window, GtkLayerShellEdge edge);
         */
        val gtk_layer_get_anchor: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_anchor", FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ), false
        )

        /**
         * void gtk_layer_set_exclusive_zone (GtkWindow *window, int exclusive_zone);
         */
        val gtk_layer_set_exclusive_zone: MethodHandle = Interop.downcallHandle(
            "gtk_layer_set_exclusive_zone", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ), false
        )

        /**
         * int gtk_layer_get_exclusive_zone (GtkWindow *window);
         */
        val gtk_layer_get_exclusive_zone: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_exclusive_zone", FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS,
            ), false
        )

        /**
         * void gtk_layer_auto_exclusive_zone_enable (GtkWindow *window);
         */
        val gtk_layer_auto_exclusive_zone_enable: MethodHandle = Interop.downcallHandle(
            "gtk_layer_auto_exclusive_zone_enable", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS
            ), false
        )

        /**
         * gboolean gtk_layer_auto_exclusive_zone_is_enabled (GtkWindow *window);
         */
        val gtk_layer_auto_exclusive_zone_is_enabled: MethodHandle = Interop.downcallHandle(
            "gtk_layer_auto_exclusive_zone_is_enabled", FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS,
            ), false
        )

        /**
         * void gtk_layer_set_keyboard_mode (GtkWindow *window, GtkLayerShellKeyboardMode mode);
         */
        val gtk_layer_set_keyboard_mode: MethodHandle = Interop.downcallHandle(
            "gtk_layer_set_keyboard_mode", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ), false
        )

        /**
         * GtkLayerShellKeyboardMode gtk_layer_get_keyboard_mode (GtkWindow *window);
         */
        val gtk_layer_get_keyboard_mode: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_keyboard_mode", FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS,
            ), false
        )

        /**
         * void gtk_layer_set_namespace (GtkWindow *window, char const* name_space);
         */
        val gtk_layer_set_namespace: MethodHandle = Interop.downcallHandle(
            "gtk_layer_set_namespace", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS,
                ValueLayout.ADDRESS,
            ), false
        )

        /**
         * const char *gtk_layer_get_namespace (GtkWindow *window);
         */
        val gtk_layer_get_namespace: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_namespace", FunctionDescriptor.of(
                ValueLayout.ADDRESS,
                ValueLayout.ADDRESS,
            ), false
        )

        /**
         * gboolean gtk_layer_is_layer_window (GtkWindow *window);
         */
        val gtk_layer_is_layer_window: MethodHandle = Interop.downcallHandle(
            "gtk_layer_is_layer_window", FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS,
            ), false
        )

        /**
         * void gtk_layer_set_monitor (GtkWindow *window, GdkMonitor *monitor);
         */
        val gtk_layer_set_monitor: MethodHandle = Interop.downcallHandle(
            "gtk_layer_set_monitor", FunctionDescriptor.ofVoid(
                ValueLayout.ADDRESS,
                ValueLayout.ADDRESS,
            ), false
        )

        /**
         * GdkMonitor *gtk_layer_get_monitor (GtkWindow *window);
         */
        val gtk_layer_get_monitor: MethodHandle = Interop.downcallHandle(
            "gtk_layer_get_monitor", FunctionDescriptor.of(
                ValueLayout.ADDRESS,
                ValueLayout.ADDRESS,
            ), false
        )
    }

    /**
     * Set the [Window][org.gnome.gtk.Window] up to be a layer surface once it is mapped. this must be called before
     * the [Window][org.gnome.gtk.Window] is realized.
     * @receiver A [Window][org.gnome.gtk.Window] to be turned into a layer surface.
     */
    fun Window.initLayer() {
        try {
            MethodHandlesInstance.gtk_layer_init_for_window.invokeExact(handle())
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
    }

    /**
     * Set the "layer" on which the surface appears (controls if it is over top of or below other surfaces). The layer may
     * be changed on-the-fly in the current version of the layer shell protocol, but on compositors that only support an
     * older version the @window is remapped so the change can take effect.
     *
     * @receiver A layer surface.
     * @param layer The layer on which this surface appears.
     * Default is [TOP][GtkLayerShell.Layer.TOP]
     */
    fun Window.setLayer(layer: Layer) {
        try {
            MethodHandlesInstance.gtk_layer_set_layer.invokeExact(handle(), layer.value)
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
    }

    /**
     * @receiver A layer surface.
     * @return the current layer.
     */
    fun Window.getLayer(): Layer {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_layer.invokeExact(handle()) as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return Layer.entries.first { it.value == result }
    }

    /**
     * @receiver: A [Window][org.gnome.gtk.Window] that may or may not have a layer surface.
     *
     * @return if [Window][org.gnome.gtk.Window] has been initialized as a layer surface.
     */
    fun Window.isLayerWindow(): Boolean {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_is_layer_window.invokeExact(handle()) as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result != 0
    }

    /**
     * NOTE: To get which monitor the surface is actually on, use
     * gdk_display_get_monitor_at_window().
     *
     * @receiver A layer surface.
     *
     * @return (transfer none): the monitor this surface will/has requested to be on, can be %NULL.
     */
    fun Window.getMonitor(): Monitor? {
        val result: MemorySegment = try {
            MethodHandlesInstance.gtk_layer_get_monitor.invokeExact(handle()) as MemorySegment
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        val obj = InstanceCache.getForType(result,
            { address: MemorySegment? ->
                Monitor(
                    address
                )
            }, true
        ) as? Monitor
        if (obj is GObject) {
            GLibLogger.debug("Ref org.gnome.gdk.Monitor %ld", obj.handle().address())
            obj.ref()
        }
        return obj
    }

    /**
     * Set the output for the window to be placed on, or %NULL to let the compositor choose.
     * If the window is currently mapped, it will get remapped so the change can take effect.
     *
     * Default is %NULL
     *
     * @receiver A layer surface.
     * @param monitor The output this layer surface will be placed on (%NULL to let the compositor decide).
     */
    fun Window.setMonitor(monitor: Monitor) {
        try {
            MethodHandlesInstance.gtk_layer_set_monitor.invokeExact(handle(), monitor.handle())
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
    }

    /**
     * Set the margin for a specific [edge] of a [Window][org.gnome.gtk.Window].
     * Effects both surface's distance from the edge
     * and its exclusive zone size (if auto exclusive zone enabled).
     *
     * Default is 0 for each [GtkLayerShell.Edge]
     *
     * @receiver A layer surface.
     * @param edge The [GtkLayerShell.Edge] for which to set the margin.
     * @param value The margin for [edge] to be set.
     *
     */
    fun Window.setMargin(edge: Edge, value: Int) {
        try {
            MethodHandlesInstance.gtk_layer_set_margin.invokeExact(handle(), edge.value, value)
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
    }

    /**
     * @receiver A layer surface.
     * @param edge the margin edge to get
     *
     * @return the size of the margin for the given edge.
     */
    fun Window.getMargin(edge: Edge): Int {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_margin.invokeExact(handle(), edge.value) as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result
    }

    /**
     * Set whether [Window][org.gnome.gtk.Window] should be anchored to [edge].
     * - If two perpendicular edges are anchored, the surface with be anchored to that corner
     * - If two opposite edges are anchored, the window will be stretched across the screen in that direction
     * Default is FALSE for each [GtkLayerShell.Edge]
     *
     * @receiver A layer surface.
     * @param edge A [GtkLayerShell.Edge] this layer surface may be anchored to.
     * @param value Whether to anchor this layer surface to [edge].
     */
    fun Window.setAnchor(edge: Edge, value: Boolean) {
        try {
            MethodHandlesInstance.gtk_layer_set_anchor.invokeExact(handle(), edge.value, if (value) 1 else 0)
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
    }

    /**
     * @receiver A layer surface.
     * @param edge the edge to which the surface may or may not be anchored
     *
     * @return if this surface is anchored to the given edge.
     */
    fun Window.getAnchor(edge: Edge): Boolean {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_anchor.invokeExact(handle(), edge.value) as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result != 0
    }

    /**
     * Has no effect unless the surface is anchored to an edge. Requests that the compositor
     * does not place other surfaces within the given exclusive zone of the anchored edge.
     * For example, a panel can request to not be covered by maximized windows.
     *
     * See [wlr-layer-shell-unstable-v1.xml](https://github.com/wmww/gtk4-layer-shell/blob/main/protocol/wlr-layer-shell-unstable-v1.xml) for details.
     *
     * Default is 0
     *
     * @receiver A layer surface.
     * @param exclusiveZone The size of the exclusive zone.
     */
    fun Window.setExclusiveZone(exclusiveZone: Int) {
        try {
            MethodHandlesInstance.gtk_layer_set_exclusive_zone.invokeExact(handle(), exclusiveZone)
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
    }

    /**
     * @receiver A layer surface.
     *
     * @return the window's exclusive zone (which may have been set manually or automatically)
     */
    fun Window.getExclusiveZone(): Int {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_exclusive_zone.invokeExact(handle()) as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result
    }

    /**
     * When auto exclusive zone is enabled, exclusive zone is automatically set to the
     * size of the @window + relevant margin. To disable auto exclusive zone, set the
     * exclusive zone to 0 or any other fixed value.
     *
     * NOTE: you can control the auto exclusive zone by changing the margin on the non-anchored
     * edge. This behavior is specific to gtk4-layer-shell and not part of the underlying protocol
     *
     * @receiver A layer surface.
     */
    fun Window.enableAutoExclusiveZone() {
        try {
            MethodHandlesInstance.gtk_layer_auto_exclusive_zone_enable.invokeExact(handle())
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
    }

    /**
     * @receiver A layer surface.
     *
     * @return if the surface's exclusive zone is set to change based on the window's size
     */
    fun Window.isAutoExclusiveZoneEnabled(): Boolean {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_auto_exclusive_zone_is_enabled.invokeExact(handle()) as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return result != 0
    }

    /**
     * Sets if/when [Window][org.gnome.gtk.Window] should receive keyboard events from the compositor, see
     * GtkLayerShellKeyboardMode for details.
     *
     * Default is [NONE][GtkLayerShell.KeyboardMode.NONE]
     *
     * @receiver A layer surface.
     * @param mode The type of keyboard interactivity requested.
     */
    fun Window.setKeyboardMode(mode: KeyboardMode) {
        try {
            MethodHandlesInstance.gtk_layer_set_keyboard_mode.invokeExact(handle(), mode.value)
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
    }

    /**
     * @receiver A layer surface.
     *
     * @return current keyboard interactivity mode for [Window][org.gnome.gtk.Window].
     */
    fun Window.getKeyboardMode(): KeyboardMode {
        val result: Int = try {
            MethodHandlesInstance.gtk_layer_get_keyboard_mode.invokeExact(handle()) as Int
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return KeyboardMode.entries.first { it.value == result }
    }

    /**
     * Set the "namespace" of the surface.
     *
     * No one is quite sure what this is for, but it probably should be something generic
     * ("panel", "osk", etc). The [namespace] string is copied, and caller maintains
     * ownership of original. If the window is currently mapped, it will get remapped so
     * the change can take effect.
     *
     * Default is "gtk4-layer-shell" (which will be used if set to %NULL)
     *
     * @receiver A layer surface.
     * @param namespace The namespace of this layer surface.
     */
    fun Window.setNamespace(namespace: String) {
        Arena.ofConfined().use { arena ->
            try {
                MethodHandlesInstance.gtk_layer_set_namespace.invokeExact(
                    handle(),
                    Interop.allocateNativeString(
                        namespace,
                        arena
                    )
                )
            } catch (e: Throwable) {
                throw AssertionError(e)
            }
        }

    }

    /**
     * NOTE: this function does not return ownership of the string. Do not free the returned string.
     * Future calls into the library may invalidate the returned string.
     *
     * @receiver A layer surface.
     * @return a reference to the namespace property. If namespace is unset, returns the
     * default namespace ("gtk4-layer-shell"). Never returns null.
     */
    fun Window.getNamespace(): String {
        val result: MemorySegment = try {
            MethodHandlesInstance.gtk_layer_get_namespace.invokeExact(handle()) as MemorySegment
        } catch (e: Throwable) {
            throw AssertionError(e)
        }
        return Interop.getStringFrom(result, false)
    }

    /**
     * @property BACKGROUND The background layer.
     * @property BOTTOM The bottom layer.
     * @property TOP The top layer.
     * @property OVERLAY The overlay layer.
     */
    enum class Layer(val value: Int) {
        BACKGROUND(0),
        BOTTOM(1),
        TOP(2),
        OVERLAY(3)
    }

    /**
     * @property LEFT The left edge of the screen.
     * @property RIGHT The right edge of the screen.
     * @property TOP The top edge of the screen.
     * @property BOTTOM The bottom edge of the screen.
     */
    enum class Edge(val value: Int) {
        LEFT(0),
        RIGHT(1),
        TOP(2),
        BOTTOM(3),
    }

    /**
     * @property NONE This window should not receive keyboard events.
     * @property EXCLUSIVE This window should have exclusive focus if it is on the top or overlay layer.
     * @property ON_DEMAND The user should be able to focus and unfocus this window in an implementation
     * defined way. Not supported for protocol version < 4.
     */
    enum class KeyboardMode(val value: Int) {
        NONE(0),
        EXCLUSIVE(1),
        ON_DEMAND(2)
    }
}
