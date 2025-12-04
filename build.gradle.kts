import com.diffplug.gradle.spotless.BaseKotlinExtension

plugins {
    kotlin("jvm") version "2.0.21"
    application
    id("com.diffplug.spotless") version "8.1.0"
}

tasks.register("runAll") {
    dependsOn.addAll(subprojects.filter { it.name != "kotlin-shared" }.map { it.tasks["run"] })
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "de.rubixdev"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "application")
    apply(plugin = "com.diffplug.spotless")

    application {
        mainClass = "de.rubixdev.aoc.MainKt"
    }

    dependencies {
        testImplementation(kotlin("test"))
    }

    kotlin {
        jvmToolchain(21)
    }

    spotless {
        fun BaseKotlinExtension.customKtlint() = ktlint("1.8.0").editorConfigOverride(
            mapOf(
                "max_line_length" to 100,
                "ktlint_standard_filename" to "disabled",
            ),
        )
        kotlin {
            targetExclude("**/*_min.kt")
            customKtlint()
        }
        kotlinGradle {
            customKtlint().editorConfigOverride(
                mapOf(
                    "ktlint_standard_no-empty-file" to "disabled",
                ),
            )
        }
    }
}

configure(subprojects.filter { it.name != "kotlin-shared" }) {
    dependencies {
        implementation(project(":kotlin-shared"))
    }
}
