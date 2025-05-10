rootProject.name = "ok-gas-station-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

// Включает вот такую конструкцию
//implementation(projects.m2l5Gradle.sub1.ssub1)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

//include(":ok-gas-station-tmp")
include(":ok-gas-station-api-v1-jackson")
include(":ok-gas-station-api-v1-mappers")
include(":ok-gas-station-api-v2-kmp")
include(":ok-gas-station-common")
