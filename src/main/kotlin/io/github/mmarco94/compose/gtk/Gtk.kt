package io.github.mmarco94.compose.gtk

import androidx.compose.runtime.Composable
import io.github.mmarco94.compose.ApplicationScope
import io.github.mmarco94.compose.initializeApplication
import org.gnome.gio.ApplicationFlags

fun application(
    appId: String,
    args: Array<String>,
    content: @Composable ApplicationScope.() -> Unit,
) {
    val app = org.gnome.gtk.Application(appId, ApplicationFlags.DEFAULT_FLAGS)
    app.initializeApplication(args, content)
}
