plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("ru.otus.kotlin.MainKt")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
}
