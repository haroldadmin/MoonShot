import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

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
    dataBinding {
        isEnabled = true
    }
    kapt {
        correctErrorTypes = true
    }
    kotlinOptions {
        this as KotlinJvmOptions
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":app"))
    implementation(project(":base"))
    implementation(project(":core"))

    implementation(Libs.kotlinStdLib)

    implementation(Libs.vector)

    implementation(Libs.appCompat)
    implementation(Libs.lifecycle)
    implementation(Libs.materialComponents)
    implementation(Libs.ktxCore)
    implementation(Libs.constraintLayout)
    implementation(Libs.navigation)

    implementation(Libs.epoxy)
    kapt(Libs.epoxyProcessor)

    implementation(Libs.coil)

    testImplementation(Libs.junit4)
    androidTestImplementation(Libs.androidxJunitExt)
    androidTestImplementation(Libs.espressoCore)
}
