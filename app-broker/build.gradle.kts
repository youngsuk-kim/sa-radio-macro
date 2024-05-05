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

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    implementation(project(":app-support"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("de.codecentric:spring-boot-admin-starter-server")
    runtimeOnly("com.mysql:mysql-connector-j")
}
