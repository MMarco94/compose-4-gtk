import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}
kotlin {
    jvmToolchain(20)
}

dependencies {
    implementation(compose.runtime)
    implementation("io.github.jwharm.javagi:gtk:0.7.1")
    implementation("io.github.jwharm.javagi:adw:0.7.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            packageName = "gtk4-demo"
            packageVersion = "1.0.0"
        }
    }
}
tasks.withType<JavaExec>().all {
    this.jvmArgs = listOf(
        "--enable-preview",
        "--enable-native-access=ALL-UNNAMED",
        "-Djava.library.path=/usr/lib64:/lib64:/lib:/usr/lib:/lib/x86_64-linux-gnu"
    )
}

tasks.withType<JavaCompile>().all {
    this.options.compilerArgs = listOf(
        "--enable-preview",
        "--enable-native-access=ALL-UNNAMED",
        "-Djava.library.path=/usr/lib64:/lib64:/lib:/usr/lib:/lib/x86_64-linux-gnu"
    )
}
