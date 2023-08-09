import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}


tasks.dokkaJavadoc.configure {

    suppressObviousFunctions.set(false)
    suppressInheritedMembers.set(true)
}

val order: String? by project

task("info") {
    println(project.name + "-" + project.version)
    println(project.version.toString())
}
taboolib {
    project.version = project.version.toString() + (order?.let { "-$it" } ?: "")
    description {
        contributors {
            name("Glom_")
        }
        dependencies {
            name("PlaceholderAPI").optional(true).loadafter(true)
            name("RandomItem").optional(true).loadbefore(true)
            name("DecentHolograms").optional(true)
            name("AttributeSystem").optional(true).loadbefore(true)
            name("MythicMobs").optional(true).loadafter(true)
            name("Adyeshach").optional(true)
        }
    }

    install(
        "common",
        "common-5",
        "module-ai",
        "module-chat",
        "module-configuration",
        "module-database",
        "module-effect",
        "module-kether",
        "module-navigation",
        "module-metrics",
        "module-database-mongodb",
        "module-nms",
        "module-nms-util",
        "module-lang",
        "module-porticus",
        "expansion-alkaid-redis"
    )
    install(
        "platform-bukkit"
    )


    classifier = null
    version = "6.0.11-31"

    relocate("org.openjdk.nashorn", "com.skillw.nashorn")
    relocate("jdk.nashorn", "com.skillw.nashorn.legacy")
    relocate("com.esotericsoftware.reflectasm", "com.skillw.asahi.reflectasm")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

tasks.register<Jar>("buildAPIJar") {
    dependsOn(tasks.compileJava, tasks.compileKotlin)
    from(tasks.compileJava, tasks.compileKotlin)
    includeEmptyDirs = false
    include { it.isDirectory or it.name.endsWith(".class") or it.name.endsWith(".kotlin_module") }
    archiveClassifier.set("api")
}

tasks.register<Jar>("buildJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks.register<Jar>("buildSourcesJar") {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

repositories {
    mavenCentral()
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    compileOnly("ink.ptms.core:v11901:11901-minimize:mapped")
    compileOnly("org.openjdk.nashorn:nashorn-core:15.4")
    compileOnly("org.codehaus.groovy:groovy-jsr223:3.0.11")
    compileOnly("com.google.code.gson:gson:2.9.0")
    compileOnly("ink.ptms.adyeshach:all:2.0.0-snapshot-1")
    compileOnly("com.esotericsoftware:reflectasm:1.11.9")
    taboo(fileTree("libs/PouNashorn.jar"))
    compileOnly(fileTree("libs"))
    compileOnly(kotlin("stdlib-jdk8"))
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


tasks.javadoc {
    this.options {
        encoding = "UTF-8"
    }
}


configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = if (project.version.toString().contains("-SNAPSHOT")) {
                uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            } else {
                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                username = project.findProperty("username").toString()
                password = project.findProperty("password").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")

            }
        }
        mavenLocal()
    }
    publications {
        create<MavenPublication>("library") {
            artifact(tasks["buildAPIJar"]) { classifier = classifier?.replace("-api", "") }
            artifact(tasks["buildJavadocJar"])
            artifact(tasks["buildSourcesJar"])
            version = project.version.toString()
            groupId = project.group.toString()
            pom {
                name.set(project.name)
                description.set("Bukkit Script Engine Plugin.")
                url.set("https://github.com/Glom-c/Pouvoir/")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/Glom-c/Pouvoir/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("Skillw")
                        name.set("Glom_")
                        email.set("glom@skillw.com")
                    }
                }
                scm {
                    connection.set("scm:git:git:https://github.com/Glom-c/Pouvoir.git")
                    developerConnection.set("scm:git:ssh:https://github.com/Glom-c/Pouvoir.git")
                    url.set("https://github.com/Glom-c/Pouvoir.git")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications.getAt("library"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}