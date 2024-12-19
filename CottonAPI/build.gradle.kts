plugins {
    id("java")
    id("fabric-loom") version "1.9-SNAPSHOT"
}

group = "net.furryplayplace"
version = "1.0.0"

repositories {
    mavenCentral()
}

var lombokVersion = property("lombok_version")
var minecraftVersion = property("minecraft_version")

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:1.21+build.9:v2")

    compileOnly("com.google.guava:guava:33.4.0-jre")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

tasks.test {
    useJUnitPlatform()
}