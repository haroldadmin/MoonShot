import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(from="../ktlint.gradle")

plugins {
    id("com.android.library")
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.incremental" to "true")
            }
        }
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
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":models"))

    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.coroutines)
    implementation(Libs.Kotlin.coroutinesAndroid)

    implementation(Libs.Koin.android)

    implementation(Libs.AndroidX.archCoreCommon)
    implementation(Libs.AndroidX.archCoreRuntime)

    implementation(Libs.Persistence.room)
    kapt(Libs.Persistence.roomCompiler)
    implementation(Libs.Persistence.roomKtx)

    implementation(Libs.jodaTime)

    testImplementation(Libs.Test.junit4)
    androidTestImplementation(Libs.Test.coroutinesTest)
    androidTestImplementation(Libs.Test.androidxJunitExt)
    androidTestImplementation(Libs.Test.espressoCore)
    androidTestImplementation(Libs.Test.archCoreTesting)
}
