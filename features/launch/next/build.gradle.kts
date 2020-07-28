plugins {
    id("com.android.dynamic-feature")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(ProjectProperties.compileSdk)

    defaultConfig {
        minSdkVersion(ProjectProperties.minSdk)
        targetSdkVersion(ProjectProperties.targetSdk)
        versionCode = ProjectProperties.versionCode
        versionName = ProjectProperties.versionName
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures.viewBinding = true
    kapt {
        correctErrorTypes = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":moonshot"))
    implementation(project(":base"))
    implementation(project(":core"))
    implementation(project(":db:spacex"))
    implementation(project(":services:spacex"))

    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.coroutines)
    implementation(Libs.Kotlin.coroutinesAndroid)

    implementation(Libs.vector)

    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.compiler)
    implementation(Libs.Dagger.AssistedInject.annotations)
    kapt(Libs.Dagger.AssistedInject.compiler)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.Lifecycle.lifecycle)
    implementation(Libs.Ui.materialComponents)
    implementation(Libs.AndroidX.ktxCore)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.navigation)
    implementation(Libs.AndroidX.navigationUi)

    implementation(Libs.Persistence.SQLDelight.coroutinesExt)
    implementation(Libs.Network.Retrofit.networkResponseAdapter4)
    implementation(Libs.Network.Moshi.moshi)

    implementation(Libs.Ui.epoxy)
    kapt(Libs.Ui.epoxyProcessor)

    testImplementation(Libs.Test.kotlinTest)
    androidTestImplementation(Libs.Test.androidxJunitExt)
    androidTestImplementation(Libs.Test.espressoCore)
}