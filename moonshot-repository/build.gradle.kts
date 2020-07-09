import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

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
        testInstrumentationRunnerArgument("runnerBuilder", "de.mannodermaus.junit5.AndroidJUnit5Builder")
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        exclude("META-INF/LICENSE*")
    }
    testOptions {
        unitTests {
            all {
                it.useJUnitPlatform()
                it.testLogging {
                    exceptionFormat = TestExceptionFormat.FULL
                    events = mutableSetOf(STARTED, SKIPPED, PASSED, FAILED)
                    showStandardStreams = true
                }
            }
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core"))
    implementation(project(":database"))
    implementation(project(":spacex-api-wrapper"))
    api(project(":models"))

    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.coroutines)

    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.compiler)
    implementation(Libs.Dagger.AssistedInject.annotations)
    kapt(Libs.Dagger.AssistedInject.compiler)

    implementation(Libs.Network.Retrofit.networkResponseAdapter)
    implementation(Libs.Network.OkHttp.loggingInterceptor)
    implementation(Libs.Network.OkHttp.okHttp)

    implementation(Libs.Ui.openGraphKt)

    coreLibraryDesugaring(Libs.coreDesugaring)

    testImplementation(Libs.Test.coroutinesTest)
    testImplementation(Libs.Test.junit4)
    testImplementation(Libs.Test.kotlinTest)
    testImplementation(Libs.Test.mockk)
}
