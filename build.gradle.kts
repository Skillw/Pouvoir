import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.izzel.taboolib") version "1.55"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(File("C:\\Users\\Administrator\\Desktop\\Doc\\pouvoir"))
    suppressObviousFunctions.set(false)
    suppressInheritedMembers.set(true)
}

val api: String? by project
val order: String? by project

task("versionModify") {
    project.version = project.version.toString() + (order?.let { "-$it" } ?: "")
}

task("versionAddAPI") {
    if (api == null) return@task
    val origin = project.version.toString()
    project.version = "$origin-api"
}


task("releaseName") {
    println(project.name + "-" + project.version)
}

task("version") {
    println(project.version.toString())
}
taboolib {
    if (project.version.toString().contains("-api")) {
        options("skip-kotlin-relocate", "keep-kotlin-module")
    }
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
    version = "6.0.11-13"

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
    taboo(fileTree("libs/Asahi-1.0.0-dev-sources.jar"))
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

java {
    withJavadocJar()
    withSourcesJar()
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
            from(components["java"])
            version = project.version.toString()
            groupId = project.group.toString()
            pom {
                name.set(project.name)
                description.set("Bukkit Script Engine Plugin.")
                url.set("https://github.com/Glom-c/Pouvoir/")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/Glom-c/Asahi/blob/main/LICENSE")
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
                    connection.set("...")
                    developerConnection.set("...")
                    url.set("...")
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