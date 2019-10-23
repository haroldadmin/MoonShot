object Libs {

    object Kotlin {
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${ProjectProperties.kotlinVersion}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${ProjectProperties.kotlinVersion}"
    }

    object Koin {
        const val koinCore = "org.koin:koin-core:${Versions.koin}"
        const val koinTest = "org.koin:koin-test:${Versions.koin}"
        const val android = "org.koin:koin-android:${Versions.koin}"
        const val scope = "org.koin:koin-androidx-scope:${Versions.koin}"
        const val viewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
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
    }

    const val vector = "com.github.haroldadmin:Vector:${Versions.vector}"

    object Persistence {
        const val room = "androidx.room:room-runtime:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    }

    const val jodaTime = "joda-time:joda-time:${Versions.jodaTime}"

    object Network {
        object Retrofit {
            const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
            const val networkResponseAdapter = "com.github.haroldadmin:CoroutinesNetworkResponseAdapter:${Versions.cnrAdapter}"
            const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.moshiConverter}"
        }
        object OkHttp {
            const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
            const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"
            const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}"
        }
        object Moshi {
            const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
            const val codegen = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
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
    }

    object Firebase {
        const val core = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
        const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"
    }
}