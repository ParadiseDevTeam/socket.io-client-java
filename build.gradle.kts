import org.gradle.internal.os.OperatingSystem

plugins {
    `java-library`
    `maven-publish`
}

group = "io.socket"
version = "1.0.2"
description = "Socket.IO Client Library for Java"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven {
        name = "prdis-releases"
        url = uri("https://repo.prdis.me/releases")
    }
}

dependencies {
    api("io.socket:engine.io-client:1.0.2-patch.1")
    implementation("org.json:json:20260814")

    testImplementation("junit:junit:4.13.2") {
        exclude(group = "org.hamcrest", module = "hamcrest-core")
    }
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testImplementation("org.skyscreamer:jsonassert:1.5.0")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:unchecked")
    options.isDeprecation = true
}

val npmInstall = tasks.register<Exec>("npmInstall") {
    workingDir = file("src/test/resources")
    commandLine = if (OperatingSystem.current().isWindows) {
        listOf("cmd", "/c", "npm", "install")
    } else {
        listOf("npm", "install")
    }
}

tasks.test {
    dependsOn(npmInstall)
    include("**/*Test.class")
    doFirst {
        // logging.properties (shared with the Maven build) still points at ./target
        file("target").mkdirs()
    }
    systemProperty("file.encoding", "UTF-8")
    systemProperty("java.util.logging.config.file", "./src/test/resources/logging.properties")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("socket.io-client")
                description.set(project.description)
                url.set("https://github.com/socketio/socket.io-client-java")
                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://opensource.org/licenses/mit-license")
                    }
                }
                developers {
                    developer {
                        id.set("nkzawa")
                        name.set("Naoyuki Kanezawa")
                        email.set("naoyuki.kanezawa@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/socketio/socket.io-client-java")
                    connection.set("scm:git:https://github.com/socketio/socket.io-client-java.git")
                    developerConnection.set("scm:git:https://github.com/socketio/socket.io-client-java.git")
                }
            }
        }
    }
    repositories {
        maven {
            name = "prdis"
            url = uri("https://repo.prdis.me/releases")
            credentials {
                username = System.getenv("REPO_PRDIS_USERNAME")
                    ?: findProperty("repo.prdis.username") as String?
                password = System.getenv("REPO_PRDIS_PASSWORD")
                    ?: findProperty("repo.prdis.password") as String?
            }
        }
    }
}
