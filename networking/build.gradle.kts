import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.uptoncedar.networking"
    compileSdk = 35

    defaultConfig {
        minSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        val properties =  Properties()
        val localProps = File(rootProject.rootDir, "local.properties")
        if(localProps.exists()) {
            localProps.inputStream().use {
                properties.load(it)
            }
        }
        val floraApiKey = properties.getProperty("flora.api.key") ?: ""
        buildConfigField("String", "FLORA_API_KEY", "\"$floraApiKey\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":common"))
    api(libs.retrofit)
    implementation(libs.converter.gson)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
}
