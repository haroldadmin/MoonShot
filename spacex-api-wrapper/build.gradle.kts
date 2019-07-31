import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
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
    implementation(Libs.kotlinStdLib)
    implementation(Libs.kotlinReflect)
    implementation(Libs.coroutines)

    implementation(Libs.koinCore)

    implementation(Libs.retrofit)
    implementation(Libs.loggingInterceptor)
    implementation(Libs.networkResponseAdapter)
    implementation(Libs.moshi)
    implementation(Libs.moshiConverter)
    implementation(Libs.moshiAdapters)
    kapt(Libs.moshiCodegen)

    testImplementation(Libs.mockWebServer)
    testImplementation(Libs.koinTest)
    testImplementation(Libs.kotlinTest)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}