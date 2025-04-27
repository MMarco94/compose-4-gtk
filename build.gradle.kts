plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.versioning)
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

group = "io.github.mmarco94"
version = "0.0.0-SNAPSHOT"

gitVersioning.apply {
    refs {
        branch(".+") {
            version = "\${ref}-SNAPSHOT"
        }
        tag("v(?<version>.*)") {
            version = "\${ref.version}\${dirty.snapshot}"
        }
    }
    rev {
        version = "\${commit.short}\${dirty.snapshot}"
    }
}

kotlin {
    jvmToolchain(23)
}

java {
    sourceCompatibility = JavaVersion.VERSION_23
    targetCompatibility = JavaVersion.VERSION_23
}

dependencies {
    api(compose.runtime)
    api(libs.javagi.gtk)
    api(libs.javagi.adw)
    implementation(libs.kotlin.logging)
    implementation(libs.slf4j.api)

    testImplementation(libs.kotlinx.datetime)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(libs.slf4j.simple)
}

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            artifact(tasks.getByName("dokkaHtmlJar"))
            artifact(tasks.getByName("kotlinSourcesJar"))
        }
    }
}

// TODO: used on examples. They should probably be moved in a separate module to avoid polluting the main build file
tasks.register("compileTestResources") {
    exec {
        workingDir = file("src/test/gresources")
        commandLine =
            listOf("glib-compile-resources", "--target=../resources/resources.gresource", "resources.gresource.xml")
    }
}

tasks.named("assembleTestResources") {
    dependsOn("compileTestResources")
}
