import java.net.URI

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${ProjectProperties.agpVersion}")
        classpath(kotlin("gradle-plugin", version = ProjectProperties.kotlinVersion))
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