apply(from = "../ktlint.gradle")

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("io.fabric")
}

android {
    compileSdkVersion(ProjectProperties.compileSdk)
    defaultConfig {
        minSdkVersion(ProjectProperties.minSdk)
        targetSdkVersion(ProjectProperties.targetSdk)
        applicationId = ProjectProperties.applicationId
        versionCode = ProjectProperties.versionCode
        versionName = ProjectProperties.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            postprocessing {
                isObfuscate = true
                isOptimizeCode = true
                isRemoveUnusedCode = true
                proguardFiles("proguard-rules.pro")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        isEnabled = true
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
    }
    kapt {
        correctErrorTypes = true
    }
    androidExtensions {
        isExperimental = true
    }
    dynamicFeatures = mutableSetOf(
        ":about",
        ":settings",
        ":rockets",
        ":launchDetails",
        ":launches",
        ":nextLaunch",
        ":launchPad",
        ":rocketDetails",
        ":search",
        ":missions"
    )
    googleServices {
        disableVersionCheck = true
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core"))
    implementation(project(":base"))
    implementation(project(":navigation"))
    implementation(project(":moonshot-repository"))

    implementation(Libs.Kotlin.stdLib)

    implementation(Libs.vector)

    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.compiler)
    implementation(Libs.Dagger.AssistedInject.annotations)
    kapt(Libs.Dagger.AssistedInject.compiler)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.Lifecycle.lifecycle)
    implementation(Libs.Ui.materialComponents)
    implementation(Libs.AndroidX.ktxCore)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.navigation)
    implementation(Libs.AndroidX.navigationUi)
    implementation(Libs.AndroidX.preference)
    implementation(Libs.AndroidX.workManager)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.Lifecycle.vmSavedState)

    implementation(Libs.jodaTime)

    implementation(Libs.Ui.epoxy)
    kapt(Libs.Ui.epoxyProcessor)

    implementation(Libs.Ui.coil)
    implementation(Libs.Ui.lemniscate)
    implementation(Libs.Ui.insetter)

    implementation(Libs.whatTheStack)

    testImplementation(Libs.Test.junit4)
    testImplementation(Libs.Test.mockkAndroid)

    androidTestImplementation(Libs.Test.mockkAndroid)
    androidTestImplementation(Libs.AndroidX.workManagerTestHelpers)
    androidTestImplementation(Libs.Test.androidxJunitExt)
    androidTestImplementation(Libs.Test.espressoCore)

    implementation(Libs.Firebase.core)
    implementation(Libs.Firebase.crashlytics)

    // Dependencies to satisfy Dagger
    implementation(project(":spacex-api-wrapper"))
    implementation(Libs.Network.OkHttp.loggingInterceptor)
    implementation(Libs.Network.Retrofit.retrofit)
    implementation(project(":database"))
    implementation(Libs.Persistence.room)
}