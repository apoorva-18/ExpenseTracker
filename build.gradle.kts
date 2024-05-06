buildscript {
    val kotlin_version by extra("2.0.0-RC1")
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")

        classpath ("com.android.tools.build:gradle:4.3.1")
    }
    repositories {

        maven {
            url = uri("https://jitpack.io")
        }
        mavenCentral()
        google()

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

//repositories {
//    google()
//}
