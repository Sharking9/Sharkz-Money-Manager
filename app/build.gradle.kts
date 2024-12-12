import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
}



android {
    namespace = "com.Sharkz.Money_Manager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.Sharkz.Money_Manager"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/DEPENDENCIES")
        }
    }


}

dependencies {

    // Google Sign-In
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    // Google Drive API
    implementation ("com.google.apis:google-api-services-drive:v3-rev20241027-2.0.0")
    implementation ("com.google.api-client:google-api-client-android:1.34.0") //depen tambahan soalnya GoogleAccountCredential gk bisa diimport
    implementation ("com.google.http-client:google-http-client-android:1.40.1") //depen tambahan soalnya GoogleAccountCredential gk bisa diimport
    // Google services plugin
    implementation ("com.google.api-client:google-api-client:2.7.1")




    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.google.android.gms:play-services-ads:23.6.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.compose.ui:ui-text-android:1.7.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}