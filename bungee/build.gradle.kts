plugins {
    java
    id("com.github.johnrengelman.shadow").version("7.1.0")
}

dependencies {
    compileOnly("org.checkerframework", "checker-qual", "3.18.1")
    compileOnly("io.github.waterfallmc", "waterfall-api", "1.17-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-platform-bungeecord:4.0.0")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set(rootProject.name)
        dependencies {
            relocate("net.kyori", "me.moros.staffchat.internal")
        }
        minimize()
    }
    build {
        dependsOn(shadowJar)
    }
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    named<Copy>("processResources") {
        filesMatching("plugin.yml") {
            expand("pluginVersion" to project.version)
        }
    }
}
