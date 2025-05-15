plugins {
    id("build-jvm")
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(projects.okGasStationCommon)
    api(projects.okGasStationRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.uuid)
    implementation(libs.db.postgres)
    implementation(libs.bundles.exposed)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.okGasStationRepoTests)
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.logback)
}
