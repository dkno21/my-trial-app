plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Tambahkan baris ini
    id("com.google.gms.google-services")
}

android {
    namespace = "com.percobaan.me"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.percobaan.me"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    // Firebase (BOM dan Firestore)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-firestore")
}
