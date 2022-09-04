val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val gson_version: String by project

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:5.2.0")
    }
}

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.0"
    id("java")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "com.doodlekong"
version = "1.0.0"
application {
    mainClass.set("com.doodlekong.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("com.google.code.gson:gson:$gson_version")
}

tasks {
    create("stage") {
        dependsOn("installDist")
    }
//    jar.enabled = false
    shadowJar {
        manifest {
            attributes("Main-Class" to mainClasses)
        }
    }
}
