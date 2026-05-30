import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
    implementation("net.java.dev.jna:jna:5.18.0")
    implementation("net.java.dev.jna:jna-platform:5.18.0")
    implementation(libs.androidx.material3.desktop)
}

compose.desktop {
    application {
        mainClass = "com.vinish.cadence.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.vinish.cadence"
            packageVersion = "1.0.0"
        }
    }
}