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
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":base"))
    implementation(project(":core"))
    implementation(project(":moonshot-repository"))

    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.coroutines)
    implementation(Libs.Kotlin.coroutinesAndroid)

    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.compiler)

    implementation(Libs.AndroidX.workManager)
    implementation(Libs.AndroidX.ktxCore)
    implementation(Libs.Ui.coil)
    implementation(Libs.AndroidX.navigation)
    implementation(Libs.jodaTime)

    testImplementation(Libs.Test.junit4)
    androidTestImplementation(Libs.Test.coroutinesTest)
    androidTestImplementation(Libs.Test.androidxJunitExt)
    androidTestImplementation(Libs.Test.espressoCore)
    androidTestImplementation(Libs.Test.archCoreTesting)
}
