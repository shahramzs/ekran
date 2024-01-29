plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.ekran"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.ekran"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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

}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.media3:media3-datasource-cronet:1.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("io.insert-koin:koin-android:3.4.0")
    implementation("io.insert-koin:koin-android-compat:3.4.0")
    implementation("io.insert-koin:koin-androidx-workmanager:3.4.0")
    implementation("io.insert-koin:koin-androidx-navigation:3.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.3.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.9")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.6.0")
    implementation("androidx.navigation:navigation-dynamic-features-runtime:2.6.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
    implementation("androidx.navigation:navigation-common-ktx:2.6.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.facebook.fresco:fresco:2.0.0")
    implementation("com.tbuonomo:dotsindicator:4.3")
    implementation("androidx.dynamicanimation:dynamicanimation-ktx:1.0.0-alpha03")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("androidx.room:room-runtime:2.5.1")
    annotationProcessor("androidx.room:room-compiler:2.5.1")
    implementation("androidx.room:room-rxjava2:2.5.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.10-1.0.9")
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.android.exoplayer:exoplayer:2.19.0")
    implementation("com.googlecode.mp4parser:isoparser:1.1.22")
    implementation("net.gotev:uploadservice:4.7.0")
    implementation ("androidx.media3:media3-exoplayer:1.1.0")
    implementation ("androidx.media3:media3-exoplayer-dash:1.1.0")
    implementation ("androidx.media3:media3-ui:1.1.0")
    implementation ("androidx.media3:media3-exoplayer-smoothstreaming:1.1.0")
    implementation ("androidx.media3:media3-exoplayer-hls:1.1.0")
    implementation ("androidx.media3:media3-exoplayer-rtsp:1.1.0")

    implementation ("com.github.Mohsen-code:kotlin-persian-date-time:0.1")

    implementation ("dev.marcelpinto:permissions-ktx:0.9")

    implementation ("io.socket:socket.io-client:2.1.0")

    implementation ("org.java-websocket:Java-WebSocket:1.5.1")

}


