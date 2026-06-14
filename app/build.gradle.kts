plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.percobaan.me"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.percobaan.me"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("ryuki-senpai-key.jks")
            // Mengambil dari parameter -P yang dikirim via .yml
            storePassword = project.findProperty("satriya12") as String? ?: ""
            keyAlias = project.findProperty("my-alias") as String? ?: ""
            keyPassword = project.findProperty("satriya12") as String? ?: ""
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }

    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output.outputFileName = "MyTrialApp.apk"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-firestore")
}
