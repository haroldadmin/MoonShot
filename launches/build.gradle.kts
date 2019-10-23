apply(from = "../ktlint.gradle")

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
    dataBinding {
        isEnabled = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":app"))
    implementation(project(":base"))
    implementation(project(":core"))
    implementation(project(":models"))
    implementation(project(":moonshot-repository"))

    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.coroutines)
    implementation(Libs.Kotlin.coroutinesAndroid)

    implementation(Libs.vector)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.Lifecycle.lifecycle)
    implementation(Libs.Ui.materialComponents)
    implementation(Libs.AndroidX.ktxCore)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.navigation)
    implementation(Libs.AndroidX.navigationUi)

    implementation(Libs.Koin.koinCore)
    implementation(Libs.Koin.android)
    implementation(Libs.Koin.viewModel)

    implementation(Libs.Ui.epoxy)
    kapt(Libs.Ui.epoxyProcessor)

    testImplementation(Libs.Test.junit4)
    androidTestImplementation(Libs.Test.androidxJunitExt)
    androidTestImplementation(Libs.Test.espressoCore)
}