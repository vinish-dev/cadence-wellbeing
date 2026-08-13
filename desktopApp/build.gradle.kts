import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(libs.kotlinx.coroutinesSwing)
    implementation("org.jetbrains.compose.material:material-icons-core:1.7.3")
    implementation(libs.compose.uiToolingPreview)
    implementation("net.java.dev.jna:jna:5.18.0")
    implementation("net.java.dev.jna:jna-platform:5.18.0")
    implementation(libs.androidx.material3.desktop)
    implementation(libs.kotlinx.serialization.json)
}

compose.desktop {
    application {
        mainClass = "com.vinish.cadence.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb,TargetFormat.Exe)
            packageName = "com.vinish.cadence"
            packageVersion = "1.0.0"
            includeAllModules = false
        }
        buildTypes.release.proguard{
            isEnabled.set(false)
        }
    }
}
