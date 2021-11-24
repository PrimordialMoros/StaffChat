plugins {
    java
}

allprojects {
    group = "me.moros"
    version = "2.0.0-SNAPSHOT"

    plugins.withId("java") {
        the<JavaPluginExtension>().toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://nexus.velocitypowered.com/repository/maven-public/")
    }
}
