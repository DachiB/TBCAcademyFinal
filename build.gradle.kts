// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.safeargs) apply false
    alias(libs.plugins.plugin.serializations) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.firebase.crashlytics") version "3.0.3" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}