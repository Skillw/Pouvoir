plugins {
    `java-library`
    id("io.izzel.taboolib") version "1.34"
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
    version = "6.0.7-44"

}

repositories {
    maven { url = uri("https://repo.tabooproject.org/storages/public/releases") }
    mavenCentral()
}

dependencies {
    compileOnly("org.codehaus.groovy:groovy-jsr223:3.0.9")
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