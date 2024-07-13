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
    jvmToolchain(22)
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

dependencies {
    api(compose.runtime)
    api(libs.javagi.gtk)
    api(libs.javagi.adw)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
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
    repositories {
        maven {
            val releasesRepoUrl = uri("https://maven.deltadelete.ru/releases")
            val snapshotsRepoUrl =  uri("https://maven.deltadelete.ru/snapshots")
            url = if (
                version.toString().endsWith("SNAPSHOT")
                ) {
                snapshotsRepoUrl
            }
            else {
                releasesRepoUrl
            }
            name = "deltadelete"
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.mmarco94"
            artifactId = "compose-4-gtk"
            from(components["kotlin"])
            artifact(tasks.getByName("dokkaHtmlJar"))
            artifact(tasks.getByName("kotlinSourcesJar"))
        }
    }
}
