plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures.viewBinding = true
    kapt {
        correctErrorTypes = true
    }
    dynamicFeatures = mutableSetOf(
        ":features:launch:all",
        ":features:launch:details",
        ":features:launch:next"
    )
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core"))
    implementation(project(":base"))
    implementation(project(":navigation"))

    implementation(project(":db:spacex"))
    implementation(project(":services:spacex"))

    implementation(Libs.Kotlin.stdLib)
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
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.navigation)
    implementation(Libs.AndroidX.navigationUi)
    implementation(Libs.AndroidX.preference)
    implementation(Libs.AndroidX.workManager)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.Lifecycle.vmSavedState)

    coreLibraryDesugaring(Libs.coreDesugaring)

    implementation(Libs.Persistence.SQLDelight.androidDriver)
    implementation(Libs.Persistence.SQLDelight.coroutinesExt)
    implementation(Libs.Network.Moshi.moshi)
    implementation(Libs.Network.Moshi.adapters)
    implementation(Libs.Network.OkHttp.loggingInterceptor)
    implementation(Libs.Network.Retrofit.retrofit)
    implementation(Libs.Network.Retrofit.moshiConverter)
    implementation(Libs.Network.Retrofit.networkResponseAdapter4)
    implementation(Libs.Network.Retrofit.scalarsConverter)

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
}