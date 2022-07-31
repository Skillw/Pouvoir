plugins {
    `java-library`
    id("org.gradle.java")
    id("io.izzel.taboolib") version "1.41"
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.dokka") version "1.6.10"
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(File("C:\\Users\\Administrator\\Desktop\\Doc\\pouvoir"))
    dokkaSourceSets {
        named("main") {
            noJdkLink.set(true)
            noStdlibLink.set(true)
            noAndroidSdkLink.set(true)
            suppressInheritedMembers.set(true)
            suppressObviousFunctions.set(false)
        }
    }
}
taboolib {
//    options("skip-kotlin-relocate")

    description {
        contributors {
            name("Glom_")
        }
        dependencies {
            name("PlaceholderAPI").optional(true).loadafter(true)
            name("MythicMobs").optional(true).loadafter(true)
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
        "module-nms",
        "module-lang",
        "module-nms-util",
        "module-porticus",
        "module-ui",
        "module-ui-receptacle",
        "expansion-player-database",
        "expansion-javascript"
    )
    install(
        "platform-bukkit"
    )


    classifier = null
    version = "6.0.9-35"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}



repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11200:11200-minimize")
    compileOnly("org.codehaus.groovy:groovy-jsr223:3.0.11")

    compileOnly("com.alibaba:fastjson:2.0.7")
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


