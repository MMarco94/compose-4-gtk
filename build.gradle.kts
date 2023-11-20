plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    api(compose.runtime)
    api("io.github.jwharm.javagi:gtk:0.8.0")
    api("io.github.jwharm.javagi:adw:0.8.0")
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
    )
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.mmarco94"
            artifactId = "compose-4-gtk"
            version = "0.1"
            from(components["kotlin"])
        }
    }
}
