plugins {
    application
    id("build-jvm")
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.gasstation.app.kafka.MainKt")
}

docker {
    javaApplication {
        baseImage.set("openjdk:${libs.versions.jvm.compiler.get()}-slim")
    }
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation(libs.gsst.logs.logback)

    implementation(project(":ok-gas-station-app-common"))

    // transport models
    implementation(project(":ok-gas-station-common"))
    implementation(project(":ok-gas-station-api-v1-jackson"))
    implementation(project(":ok-gas-station-api-v1-mappers"))
    implementation(project(":ok-gas-station-api-v2-kmp"))
    // logic
    implementation(project(":ok-gas-station-biz"))

    testImplementation(kotlin("test-junit"))
}
