plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.safeargs)
    alias(libs.plugins.plugin.serializations)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {

    namespace = "com.example.tbcacademyfinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tbcacademyfinal"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://reqres.in/\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://reqres.in/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    packaging { // KEEP THIS BLOCK
        resources {
            pickFirsts += "google/protobuf/descriptor.proto"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs.pickFirsts.add("lib/**/libarcore_sdk_c.so")
        jniLibs.pickFirsts.add("lib/**/libarcore_sdk_java.so")
    }

    //NO Compress?
}

dependencies {
    implementation(libs.androidx.material)
    implementation(libs.androidx.compiler)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.core)
    implementation(libs.datastore)
    implementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit)
    implementation(libs.play.services.maps)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.glide)
    implementation(libs.androidx.room.ktx)
    implementation(libs.paging)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.datastore)
    implementation(libs.retrofit2.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)
    implementation(libs.hilt.android)
    implementation(libs.coil.compose)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.sceneview)
    implementation(libs.ar.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.room.compiler)
    ksp(libs.moshi.kotlin.codegen.v1140)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

