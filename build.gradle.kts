plugins {
    kotlin("multiplatform") version "1.5.21"
}

group = "xyz.eatsteak"
version = "0.1.0-SNAPSHOT"

val ktorVersion: String by project
val runBlockingVersion: String by project

repositories {
    mavenCentral()
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
                useMocha()
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
                implementation("com.javiersc.run-blocking:suspend-test:$runBlockingVersion")
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
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation(npm("brotli", "1.3.2"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
