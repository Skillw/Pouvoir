plugins {
    java
    id("io.izzel.taboolib") version "1.16"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    install("common")
    install("platform-bukkit")
    classifier = null
    version = "6.0.0-pre40"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}