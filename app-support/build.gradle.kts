tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}
