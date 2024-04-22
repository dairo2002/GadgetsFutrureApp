plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.gadgetsfuture"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.gadgetsfuture"
        minSdk = 29
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    //Libreria circulo de imagen de perfil
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.material:material:1.11.0")
    //Librerias carrusel
    //implementation ("com.github.denzcoskun:ImageSlideshow:0.1.0")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.0")

    implementation ("com.squareup.okhttp3:okhttp:4.9.2")

    //Librerias conexion de API
    implementation("com.android.volley:volley:1.2.1")
    //librería para trabajo en segundo plano
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // Si la API devuelve JSON y quieres convertirlo automáticamente a objetos Kotlin

    //Predeterminadas
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}