plugins {
    `java-library`
    id("io.izzel.taboolib") version "1.34"
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

    install(
        "common",
        "common-5",
        "module-configuration",
        "module-lang",
        "platform-bukkit",
        "module-metrics",
        "module-chat",
        "module-nms-util",
        "module-nms",
        "module-database"
    )

    classifier = null
    version = "6.0.7-17"
}

repositories {
    maven { url = uri("https://repo.tabooproject.org/storages/public/releases") }
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v11605:11605")

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