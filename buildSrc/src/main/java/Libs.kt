object Libs {

    object Kotlin {
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${ProjectProperties.kotlinVersion}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${ProjectProperties.kotlinVersion}"
    }

    object Dagger {
        const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"

        object AssistedInject {
            const val annotations = "com.squareup.inject:assisted-inject-annotations-dagger2:${Versions.assistedInject}"
            const val compiler = "com.squareup.inject:assisted-inject-processor-dagger2:${Versions.assistedInject}"
        }
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val ktxCore = "androidx.core:core-ktx:${Versions.ktxCore}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
        const val preference = "androidx.preference:preference-ktx:${Versions.preference}"
        const val workManager = "androidx.work:work-runtime-ktx:${Versions.workManager}"
        const val workManagerTestHelpers = "androidx.work:work-testing:${Versions.workManager}"

        const val archCoreCommon = "androidx.arch.core:core-common:${Versions.archCore}"
        const val archCoreRuntime = "androidx.arch.core:core-runtime:${Versions.archCore}"

        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
    }

    object Lifecycle {
        const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val vmSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.vmSavedState}"
    }

    object Ui {
        const val materialComponents = "com.google.android.material:material:${Versions.materialComponents}"
        const val epoxy = "com.airbnb.android:epoxy:${Versions.epoxy}"
        const val epoxyProcessor = "com.airbnb.android:epoxy-processor:${Versions.epoxy}"
        const val coil = "io.coil-kt:coil:${Versions.coil}"
        const val lemniscate = "com.github.VladimirWrites:Lemniscate:${Versions.lemniscate}"
        const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
        const val openGraphKt = "com.github.haroldadmin:opengraphKt:${Versions.openGraphKt}"
        const val insetter = "dev.chrisbanes:insetter-ktx:${Versions.insetter}"
    }


    object Persistence {
        const val room = "androidx.room:room-runtime:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    }

    object Network {
        object Retrofit {
            const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
            const val networkResponseAdapter = "com.github.haroldadmin:NetworkResponseAdapter:${Versions.cnrAdapter}"
            const val networkResponseAdapter4 = "com.github.haroldadmin:NetworkResponseAdapter:${Versions.networkResponseAdapter}"
            const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.moshiConverter}"
            const val scalarsConverter = "com.squareup.retrofit2:converter-scalars:${Versions.retrofit}"
        }
        object OkHttp {
            const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
            const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"
            const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}"
        }
        object Moshi {
            const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
            const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
            const val adapters = "com.squareup.moshi:moshi-adapters:${Versions.moshiAdapters}"
        }
    }

    object Test {
        const val junit4 = "junit:junit:${Versions.junit4}"
        const val androidxJunitExt = "androidx.test.ext:junit:${Versions.androidxJunitExt}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
        const val kotlinTest = "io.kotlintest:kotlintest-runner-junit5:${Versions.kotlinTest}"
        const val androidxTestCore = "androidx.arch.core:core-testing:${Versions.androidxTestCore}"
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
        const val mockkAndroid = "io.mockk:mockk-android:${Versions.mockk}"
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val archCoreTesting = "androidx.arch.core:core-testing:${Versions.archCore}"
    }

    object Firebase {
        const val core = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
        const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"
    }

    const val vector = "com.github.haroldadmin:Vector:${Versions.vector}"
    const val whatTheStack = "com.github.haroldadmin:WhatTheStack:${Versions.whatTheStack}"
    const val jodaTime = "joda-time:joda-time:${Versions.jodaTime}"
    const val coreDesugaring = "com.android.tools:desugar_jdk_libs:${Versions.coreDesugaring}"
}