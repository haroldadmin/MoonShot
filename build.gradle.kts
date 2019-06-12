buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${ProjectProperties.agpVersion}")
        classpath(kotlin("gradle-plugin", version = ProjectProperties.kotlinVersion))
        classpath(Libs.safeArgs)
        classpath("com.google.gms:google-services:${ProjectProperties.gmsVersion}")
        classpath("io.fabric.tools:gradle:${ProjectProperties.fabricVersion}")
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