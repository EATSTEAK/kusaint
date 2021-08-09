@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform") version "1.5.21"
    id("org.jetbrains.dokka") version "1.5.0"
}

group = "xyz.eatsteak"
version = "0.1.0-SNAPSHOT"

val ktorVersion: String by project
val runBlockingVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.5.0")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js(LEGACY) {
        nodejs {
            testTask {
                debug = false
                useMocha {
                    timeout = "10000000"
                }
            }
        }
        binaries.executable()
    }

    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-encoding:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
                kotlin("reflect")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-java:$ktorVersion")
                implementation("org.brotli:dec:0.1.2")
                implementation("org.brotli:parent:0.1.2")
                implementation("org.jsoup:jsoup:1.14.1")
            }
        }
        val jvmTest by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation(npm("ssl-root-cas", "1.3.1"))
                implementation(npm("jsdom", "16.7.0"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

tasks {
    withType<ProcessResources>().named("jsProcessResources") {
        into("$buildDir/js/packages/kusaint")
    }
}