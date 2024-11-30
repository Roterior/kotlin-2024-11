rootProject.name = "otus-kotlin-2024-11"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

include("m1l1-first")
