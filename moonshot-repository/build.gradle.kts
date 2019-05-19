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

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core"))
    implementation(project(":database"))
    implementation(project(":spacex-api-wrapper"))
    api(project(":models"))

    implementation(Libs.kotlinStdLib)
    implementation(Libs.coroutines)
    implementation(Libs.rxJava)

    implementation(Libs.koinAndroid)

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.koinTest)
    androidTestImplementation(Libs.androidxJunitExt)
    androidTestImplementation(Libs.espressoCore)
    androidTestImplementation(Libs.androidxTestCore)
}
