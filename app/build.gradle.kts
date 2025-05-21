plugins {
    alias(libs.plugins.android.application)
    // If you don’t already have the Kotlin Android plugin, add:
    // alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.lostfoundapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lostfoundapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // If you’re writing any Kotlin code, add this block:
    // kotlinOptions {
    //     jvmTarget = "11"
    // }
}

dependencies {
    // Core UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Google Maps SDK
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Google Places Autocomplete
    implementation("com.google.android.libraries.places:places:3.4.0")

    // Fused Location Provider
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // Test libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
