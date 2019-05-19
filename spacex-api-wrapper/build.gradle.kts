plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.kotlinStdLib)
    implementation(Libs.coroutines)

    implementation(Libs.koinCore)

    implementation(Libs.retrofit)
    implementation(Libs.loggingInterceptor)
    api(Libs.networkResponseAdapter)
    implementation(Libs.moshi)
    implementation(Libs.moshiConverter)
    implementation (Libs.moshiAdapters)
    kapt(Libs.moshiCodegen)

    testImplementation(Libs.mockWebServer)
    testImplementation(Libs.junit)
    testImplementation(Libs.koinTest)
    testImplementation(Libs.kotlinTest)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}