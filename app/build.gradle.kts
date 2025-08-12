plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("dagger.hilt.android.plugin") // Required for Hilt setup
    kotlin("kapt")
}

android {
    namespace = "com.ppp.pegasussociety"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ppp.pegasussociety"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.books)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.test.junit4.android)
    implementation(libs.androidx.databinding.adapters)
    implementation(libs.androidx.compose.navigation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")


    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Show Custom Toast
    //   implementation ("com.github.GrenderG://Toasty:1.5.2")
    // Custom icons
    implementation (libs.androidx.material.icons.extended)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")


    // Hilt dependencies
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    // for hiltViewModel()
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    //Retrofit
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.hilt:hilt-navigation:1.2.0")

    // implementation ("com.google.accompanist:accompanist-insets:0.31.2-alpha")

    // implementation ("com.google.accompanist:accompanist-insets:0.32.0-alpha")

    // implementation("com.google.accompanist:accompanist-permission:0.33.0-alpha")

    // In-App update
    implementation("com.google.android.play:app-update:2.1.0")
    // For Kotlin users also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // Google Maps
    implementation("com.google.maps.android:maps-compose:4.3.3")

    //  implementation("androdix.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")

    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    implementation ("com.google.accompanist:accompanist-permissions:0.31.1-alpha")

    //  implementation("com.google.accompanist:accompanist-permissions:0.32.0")
    implementation ("com.github.0xRahad:RioBottomNavigation:1.0.2")

    implementation ("androidx.navigation:navigation-compose:2.7.6") // or latest
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.34.0") // or latest
    //  implementation("androdix.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")

    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    //-------Text dependency
    implementation("com.google.mlkit:text-recognition:16.0.0")

    implementation ("com.google.mlkit:object-detection:17.0.0")
    implementation ("com.google.mlkit:vision-common:17.0.2")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")

    implementation ("com.google.accompanist:accompanist-pager:0.30.1")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")



    implementation("com.msg91.lib:sendotp:1.0.0")

    /*  implementation 'com.google.android.gms:play-services-auth:20.7.0'
      implementation 'com.google.android.gms:play-services-auth-api-phone:18.0.1'
  */// Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.mlkit:text-recognition:16.0.0")

    implementation("com.google.android.play:app-update-ktx:2.1.0")


// Facebook Login
    // implementation("com.facebook.android:facebook-login:latest.release")

    // implementation("androidx.compose.compose.ui:ui-textgoogle_fonts:1.6.0")

    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")

    // Room components

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    //country code api
    implementation("com.github.ahmmedrejowan:CountryCodePickerCompose:0.1")

    androidTestImplementation ("androidx.compose.ui:ui-test-junit4")
    debugImplementation ("androidx.compose.ui:ui-test-manifest")

    // Required for Compose UI testing
    //  androidTestImplementation( "androidx.compose.ui:ui-test-junit4")

// Required for idling resources (avoids the crash)
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

// Optional: for UI testing helpers and matchers
    androidTestImplementation ("androidx.test.espresso:espresso-idling-resource:3.5.1")


    implementation ("com.google.accompanist:accompanist-placeholder-material:0.34.0")

    //    bar chart dependency
    implementation("com.patrykandpatrick.vico:core:1.13.1")
    implementation("com.patrykandpatrick.vico:compose:1.13.1")
    implementation("com.patrykandpatrick.vico:compose-m3:1.13.1")

    // Other Compose dependencies
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")



// Required for Compose test runner
    implementation(kotlin("script-runtime"))

    //------------Lottie Animation----------//
    implementation ("com.airbnb.android:lottie-compose:6.0.0")
    implementation ("com.google.accompanist:accompanist-pager:0.30.1")

    implementation ("androidx.compose.ui:ui:1.5.0")
    implementation ("androidx.compose.material3:material3:1.1.0")



    implementation("com.google.code.gson:gson:2.10.1")


}



// Allow references to generated code
kapt {
    correctErrorTypes = true
}