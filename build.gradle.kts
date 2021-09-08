plugins {
    kotlin("jvm") version "1.5.20"
    `maven-publish`
    signing
}

group = "io.github.c-classen.oakdsl"
version = "0.1.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.4")
    testImplementation("org.assertj:assertj-core:3.20.2")
}

tasks.test {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("oak-dsl")
                description.set("OpenAPI Kotlin Domain Specific Language")
                url.set("https://github.com/c-classen/oak-dsl")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        name.set("Clemens Classen")
                        email.set("clemens.classen@web.de")
                    }
                }
                scm {
                    connection.set("https://github.com/c-classen/oak-dsl.git")
                    url.set("https://github.com/c-classen/oak-dsl")
                }
            }
        }
    }
    repositories {
        maven {
            name = "OSSRH"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = System.getenv("OSSRH_USER") ?: return@credentials
                password = System.getenv("OSSRH_PASSWORD") ?: return@credentials
            }
        }
    }
}

signing {
    val key = System.getenv("SIGNING_KEY")
    val password = System.getenv("SIGNING_PASSWORD")
    val publishing: PublishingExtension by project

    useInMemoryPgpKeys(key, password)
    sign(publishing.publications)
}
