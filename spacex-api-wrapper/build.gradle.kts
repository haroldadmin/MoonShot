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
    implementation(project(":core"))

    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.reflect)
    implementation(Libs.Kotlin.coroutines)

    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.compiler)
    implementation(Libs.Dagger.AssistedInject.annotations)
    kapt(Libs.Dagger.AssistedInject.compiler)

    implementation(Libs.Network.Retrofit.retrofit)
    implementation(Libs.Network.OkHttp.loggingInterceptor)
    implementation(Libs.Network.Retrofit.networkResponseAdapter)
    implementation(Libs.Network.Moshi.moshi)
    implementation(Libs.Network.Retrofit.moshiConverter)
    implementation(Libs.Network.Moshi.adapters)
    kapt(Libs.Network.Moshi.codegen)

    testImplementation(Libs.Network.OkHttp.mockWebServer)
    testImplementation(Libs.Test.kotlinTest)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}