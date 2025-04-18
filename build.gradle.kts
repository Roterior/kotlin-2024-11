plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "com.otus.otuskotlin.gasstation"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

tasks {
    create("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }

    val buildImages: Task by creating {
        dependsOn(gradle.includedBuild("ok-gas-station-be").task(":buildImages"))
    }

    create("check") {
        group = "verification"
        dependsOn(buildImages)
    }
}
