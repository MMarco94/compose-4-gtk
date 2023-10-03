package io.github.mmarco94.compose

import org.gnome.gio.ApplicationFlags
import org.gnome.adw.*
import org.gnome.adw.Application
import org.gnome.adw.ApplicationWindow
import org.gnome.adw.HeaderBar
import org.gnome.gtk.*

fun main() {
    val app = Application("my.example.HelloApp", ApplicationFlags.DEFAULT_FLAGS)
    app.onActivate { activate(app) }
    app.run(emptyArray())
}

fun activate(app: Application) {
    val window = ApplicationWindow(app)
    window.setTitle("GTK from Kotlin")
    window.setDefaultSize(800, 480)

    val mainPane = Box.builder()
        .orientation(Orientation.VERTICAL)
        .hexpand(true)
        .build()
    mainPane.append(HeaderBar.builder().build())
    val toastContainer = ToastOverlay.builder().build()
    mainPane.append(toastContainer)

    val box = Box.builder()
        .orientation(Orientation.VERTICAL)
        .halign(Align.CENTER)
        .valign(Align.CENTER)
        .build()
    box.append(Button.newWithLabel("Test?").apply {
        onClicked {
            toastContainer.addToast(Toast.builder().title("Test!").build())
        }
    })
//    Entry.builder().build().apply {
//        //onChanged { println("On changed") }
//        onInsertText { text, length, position -> println("On insert $text") }
//        //onDeleteText { startPos, endPos -> println("On delete") }
//        text = "asdasduhv"
//    }

    val flap = ScrolledWindow.builder()
        .hscrollbarPolicy(PolicyType.NEVER)
        .vscrollbarPolicy(PolicyType.AUTOMATIC)
        .child(
            Box.builder()
                .orientation(Orientation.VERTICAL)
                .vexpand(true)
                .widthRequest(250)
                .build()
                .apply {
                    repeat(50) {
                        append(Button.newWithLabel("Button $it"))
                    }
                }
        ).build()


    val panes = Flap.builder()
        .content(box)
        .flap(flap)
        .build()
    toastContainer.child = panes

    window.content = mainPane
    window.present()
}
