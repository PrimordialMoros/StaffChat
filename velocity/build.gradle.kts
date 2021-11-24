plugins {
    java
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
}

dependencies {
    compileOnly("org.checkerframework", "checker-qual", "3.18.1")
    compileOnly("com.velocitypowered", "velocity-api", "3.0.1")
    annotationProcessor("com.velocitypowered", "velocity-api", "3.0.1")
}
