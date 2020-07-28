import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("SpacexDatabase") {
        packageName = "com.haroldadmin.moonshot.db.spacex"
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("started", "skipped", "passed", "failed")
        showStandardStreams = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":services:spacex"))

    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.coroutines)
    implementation(Libs.Persistence.SQLDelight.jvmDriver)

    testImplementation(Libs.Test.kotlinTest)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}