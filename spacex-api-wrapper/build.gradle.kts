plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.kotlinStdLib)
    implementation(Libs.coroutines)

    implementation(Libs.retrofit)
    implementation(Libs.loggingInterceptor)
    implementation(Libs.networkResponseAdapter)
    implementation(Libs.moshi)
    kapt(Libs.moshiCodegen)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}