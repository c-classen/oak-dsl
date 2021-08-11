plugins {
    kotlin("jvm") version "1.5.20"
    `maven-publish`
}

group = "com.github.cclassen.oakdsl"
version = "0.1.0-SNAPSHOT"

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.cclassen.oakdsl"
            artifactId = "oakdsl"
            version = "0.1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}
