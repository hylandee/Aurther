plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    `java-library`
    `maven-publish`
    signing
}

group = "io.github.hylandee"
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
    jvmToolchain(21)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("Aurther") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "Aurther"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}
