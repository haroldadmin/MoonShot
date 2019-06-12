apply(from="../ktlint.gradle")

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
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        all {
            if (project.hasProperty("MAPQUEST_API_KEY")) {
                val MAPQUEST_API_KEY: String by project
                buildConfigField("String", "MapQuestApiKey", MAPQUEST_API_KEY)
            } else {
                buildConfigField("String", "MapQuestApiKey", "mock-key")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dataBinding {
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
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core"))
    implementation(project(":base"))
    implementation(project(":moonshot-repository"))

    implementation(Libs.kotlinStdLib)

    implementation(Libs.koinAndroid)
    implementation(Libs.koinViewModel)
    implementation(Libs.koinScope)

    implementation(Libs.vector)

    implementation(Libs.rxJava)
    implementation(Libs.rxAndroid)

    implementation(Libs.appCompat)
    implementation(Libs.lifecycle)
    implementation(Libs.materialComponents)
    implementation(Libs.ktxCore)
    implementation(Libs.constraintLayout)
    implementation(Libs.navigation)
    implementation(Libs.navigationUi)
    implementation(Libs.preference)

    implementation(Libs.epoxy)
    implementation(Libs.epoxyDatabinding)
    kapt(Libs.epoxyProcessor)

    implementation(Libs.glide)
    kapt(Libs.glideCompiler)

    implementation(Libs.lemniscate)

    testImplementation(Libs.junit4)
    androidTestImplementation(Libs.androidxJunitExt)
    androidTestImplementation(Libs.espressoCore)

    implementation(Libs.firebaseCore)
    implementation(Libs.crashlytics)
}