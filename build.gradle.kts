
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("com.google.gms.google-services") version "4.5.0" apply false
    id("com.google.gms.google-services")
}

implementation(platform("com.google.firebase:firebase-bom:34.16.0"))
implementation("com.google.firebase:firebase-firestore")
implementation(project(":shared"))