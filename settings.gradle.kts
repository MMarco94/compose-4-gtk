pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        id("org.jetbrains.kotlin.jvm")
        id("org.jetbrains.compose")
        id("org.jetbrains.kotlin.plugin.compose")
    }
}

rootProject.name = "compose-4-gtk"
