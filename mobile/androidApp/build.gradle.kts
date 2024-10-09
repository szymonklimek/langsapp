plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.langsapp.android.app"

    compileSdk = 34
    defaultConfig {
        applicationId = "com.langsapp.android"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders += mapOf("appAuthRedirectScheme" to "langsapp")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":app"))

    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("com.auth0.android:jwtdecode:2.0.2")
    implementation("net.openid:appauth:0.11.1")
}
