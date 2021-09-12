plugins {
    java
    id("io.izzel.taboolib") version "1.26"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    description {
        contributors {
            name("Glom_")
        }
        dependencies {
            name("PlaceholderAPI").optional(true).loadafter(true)
            name("MythicMobs").optional(true).loadafter(true)
            name("AttributeSystem").optional(true).loadbefore(true)
        }
    }

    install("common")
    install("module-configuration")
    install("module-lang")
    install("platform-bukkit")
    install("module-metrics")
    install("module-chat")
    install("module-nms")
    install("module-nms-util")
    classifier = null
    version = "6.0.2-1"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v11604:11604:all")
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