package io.github.compose4gtk.gtk

import androidx.compose.runtime.Composable
import io.github.compose4gtk.ApplicationScope
import io.github.compose4gtk.initializeApplication
import org.gnome.gio.ApplicationFlags

fun application(
    appId: String,
    args: Array<String>,
    content: @Composable ApplicationScope.() -> Unit,
) {
    val app = org.gnome.gtk.Application(appId, ApplicationFlags.DEFAULT_FLAGS)
    app.initializeApplication(args, content)
}
