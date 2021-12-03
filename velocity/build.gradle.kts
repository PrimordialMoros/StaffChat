plugins {
    java
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
}

dependencies {
    compileOnly("org.checkerframework", "checker-qual", "3.18.1")
    compileOnly("com.velocitypowered", "velocity-api", "3.1.0")
    annotationProcessor("com.velocitypowered", "velocity-api", "3.1.0")
}
