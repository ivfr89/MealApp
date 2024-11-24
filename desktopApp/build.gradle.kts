import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    kotlin("jvm") version "2.0.0"
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    application
}

dependencies {
    implementation(project(":composeApp"))
    implementation(compose.desktop.currentOs)
}

application {
    mainClass.set("com.ivan.mealapp.MealAppKt")
}

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Zip>{
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}