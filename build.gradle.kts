plugins {
    java
    id("com.github.johnrengelman.shadow").version("7.1.0")
}

group = "me.moros"
version = "1.0.1-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(16))
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation("net.kyori:adventure-platform-bungeecord:4.0.0")
    compileOnly("org.checkerframework", "checker-qual", "3.18.1")
    compileOnly("io.github.waterfallmc", "waterfall-api", "1.17-R0.1-SNAPSHOT")
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
