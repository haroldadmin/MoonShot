import org.gradle.api.tasks.testing.logging.TestExceptionFormat

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
    }
    packagingOptions {
        exclude("META-INF/LICENSE*")
    }
    testOptions {
        unitTests.apply {
            all(KotlinClosure1<Test, Test>({
                useJUnitPlatform { }
                testLogging {
                    exceptionFormat = TestExceptionFormat.FULL
                    events("started", "skipped", "passed", "failed")
                    showStandardStreams = true
                }
                this
            }, this))
        }
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

    implementation(Libs.koinAndroid)

    implementation(Libs.networkResponseAdapter)
    implementation(Libs.okHttp)

    testImplementation(Libs.coroutinesTest)
    testImplementation(Libs.koinTest)
    testImplementation(Libs.kotlinTest)
    testImplementation(Libs.mockk)
}
