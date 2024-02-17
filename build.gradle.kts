plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    `java-library`
    `maven-publish`
    signing
}

group = "hylandee"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.9")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.0")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

java {
    withJavadocJar()
    withSourcesJar()
}
