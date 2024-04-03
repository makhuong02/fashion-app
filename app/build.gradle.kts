plugins {
    id("com.android.application")
}

android {
    namespace = "com.group25.ecommercefashionapp"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    defaultConfig {
        applicationId = "com.group25.ecommercefashionapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.17")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("androidx.databinding:databinding-runtime:8.1.4")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("androidx.navigation:navigation-fragment:2.7.4")
    implementation ("androidx.navigation:navigation-ui:2.7.4")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.9.1")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.gms:play-services-places:17.0.0")
    implementation ("com.google.android.libraries.places:places:3.3.0")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.0")
    implementation ("com.github.chrisbanes:PhotoView:2.0.0")
    implementation ("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation ("io.jsonwebtoken:jjwt-impl:0.12.5")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}