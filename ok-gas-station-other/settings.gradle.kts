rootProject.name = "ok-gas-station-other"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":ok-gas-station-migration-pg")
