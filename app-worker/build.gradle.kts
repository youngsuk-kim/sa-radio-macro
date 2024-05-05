dependencyManagement {
    imports {
        mavenBom("de.codecentric:spring-boot-admin-dependencies:3.2.3")
    }
}
tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

dependencies {
    implementation(project(":app-support"))
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("de.codecentric:spring-boot-admin-starter-client:3.2.3")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("io.asyncer:r2dbc-mysql:1.1.0")
    implementation("io.r2dbc:r2dbc-h2:1.0.0.RELEASE")
    implementation("io.r2dbc:r2dbc-spi")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-aarch_64")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    implementation("com.github.ben-manes.caffeine:caffeine:2.8.8") // 캐싱을 위한 Caffeine 라이브러리
    implementation("io.github.serpro69:kotlin-faker:1.15.0")
}
