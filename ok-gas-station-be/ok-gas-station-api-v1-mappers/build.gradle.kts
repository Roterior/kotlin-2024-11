plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ok-gas-station-api-v1-jackson"))
    implementation(project(":ok-gas-station-common"))

    testImplementation(kotlin("test-junit"))
}
