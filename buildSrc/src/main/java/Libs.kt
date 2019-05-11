object Libs {

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${ProjectProperties.kotlinVersion}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktxCore}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"

    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomRxJava = "androidx.room:room-rxjava2:${Versions.room}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"
    const val networkResponseAdapter = "com.github.haroldadmin:NetworkResponse-Retrofit-Call-Adapter-Coroutines:${Versions.cnrAdapter}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiCodegen = "com.squareup.moshiCodegen:moshiCodegen-kotlin-codegen:${Versions.moshi}"

    const val junit = "junit:junit:${Versions.junit}"
    const val androidxJunitExt = "androidx.test.ext:junit:${Versions.androidxJunitExt}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
}