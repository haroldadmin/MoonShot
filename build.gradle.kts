buildscript {
    val kotlin_version by extra("1.3.72")
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${ProjectProperties.agpVersion}")
        classpath(kotlin("gradle-plugin", version = ProjectProperties.kotlinVersion))
        classpath(Libs.AndroidX.safeArgs)
        classpath("com.google.gms:google-services:${ProjectProperties.gmsVersion}")
        classpath("io.fabric.tools:gradle:${ProjectProperties.fabricVersion}")
        classpath(Libs.Persistence.SQLDelight.gradlePlugin)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}