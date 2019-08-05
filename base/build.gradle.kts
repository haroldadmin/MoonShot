import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

apply(from="../ktlint.gradle")

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(ProjectProperties.compileSdk)
    defaultConfig {
        minSdkVersion(ProjectProperties.minSdk)
        targetSdkVersion(ProjectProperties.targetSdk)
        versionCode = ProjectProperties.versionCode
        versionName = ProjectProperties.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        this as KotlinJvmOptions
        jvmTarget = "1.8"
    }
}



dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":core"))

    implementation(Libs.kotlinStdLib)
    implementation(Libs.coroutines)
    implementation(Libs.coroutinesAndroid)

    implementation(Libs.vector)
    implementation(Libs.appCompat)
    implementation(Libs.vmSavedState)
    implementation(Libs.ktxCore)

    implementation(Libs.epoxy)

    testImplementation(Libs.junit4)
    androidTestImplementation(Libs.mockkAndroid)
    androidTestImplementation(Libs.androidxJunitExt)
    androidTestImplementation(Libs.espressoCore)
    androidTestImplementation(Libs.androidxTestCore)
}
