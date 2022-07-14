plugins {
    `java-library`
    id("io.izzel.taboolib") version "1.40"
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.dokka") version "1.6.10"
}



tasks.dokkaJavadoc.configure {
    val dokkaPath = projectDir.absolutePath.replace(rootDir.absolutePath, "")
    outputDirectory.set(File(rootDir.absolutePath + File.separator + "dokka" + dokkaPath))
    dokkaSourceSets {
        named("main") {
            noJdkLink.set(true)
            noStdlibLink.set(true)
            noAndroidSdkLink.set(true)
            suppressInheritedMembers.set(true)
            suppressObviousFunctions.set(false)
            sourceRoots.from(
                file("src/main/kotlin/com/skillw/pouvoir/api"),
                file("src/main/kotlin/com/skillw/pouvoir/util"),
                file("src/main/kotlin/com/skillw/pouvoir/internal/annotation")
            )
        }
    }
}


taboolib {
    options("skip-kotlin-relocate")

    description {
        contributors {
            name("Glom_")
        }
        dependencies {
            name("MythicMobs").optional(true).loadafter(true)
            name("PlaceholderAPI").optional(true).loadafter(true)
            name("RandomItem").optional(true).loadbefore(true)
            name("AttributeSystem").optional(true).loadbefore(true)
        }
    }

    install(
        "common",
        "common-5",
        "module-ai",
        "module-chat",
        "module-configuration",
        "module-database",
        "module-database-mongodb",
        "module-effect",
        "module-kether",
        "module-metrics",
        "module-navigation",
        "module-lang",
        "module-nms",
        "module-nms-util",
        "module-porticus",
        "module-ui",
        "module-ui-receptacle",
        "platform-bukkit",
        "expansion-player-database",
        "expansion-javascript"
    )

    options()

    classifier = null
    version = "6.0.9-26"

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}


repositories {
    maven { url = uri("https://repo.nukkitx.com/maven-snapshots") }
    maven { url = uri("https://repo.spongepowered.org/maven") }
    maven { url = uri("https://nexus.velocitypowered.com/repository/maven-public/") }
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11900:11900-minimize:mapped")
    compileOnly("ink.ptms.core:v11900:11900-minimize:universal")
    compileOnly("org.codehaus.groovy:groovy-jsr223:3.0.9")

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


