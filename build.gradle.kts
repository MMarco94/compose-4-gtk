plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

kotlin {
    jvmToolchain(22)
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

dependencies {
    implementation(compose.runtime)
    implementation(libs.javagi.gtk)
    implementation(libs.javagi.adw)
}

tasks.withType<JavaExec>().all {
    this.environment("GDK_BACKEND", "wayland")
    this.jvmArgs = listOf(
        "--enable-preview",
        "--enable-native-access=ALL-UNNAMED",
        "-Djava.library.path=/usr/lib"
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
