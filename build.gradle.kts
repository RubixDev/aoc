import com.diffplug.gradle.spotless.BaseKotlinExtension

plugins {
    kotlin("jvm") version "2.0.21"
    id("scala")
    application
    id("com.diffplug.spotless") version "8.1.0"
}

tasks.register("runAll") {
    dependsOn.addAll(subprojects.filter { "shared" !in it.name }.map { it.tasks["run"] })
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "de.rubixdev"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "application")
    apply(plugin = "com.diffplug.spotless")

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
        scala {
            scalafmt("3.10.2").configFile(rootDir.resolve(".scalafmt.conf"))
        }
    }
}

configure(subprojects.filter { "kotlin" in it.name }) {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        testImplementation(kotlin("test"))
    }

    kotlin {
        jvmToolchain(21)
    }
}

configure(subprojects.filter { "kotlin" in it.name && it.name != "kotlin-shared" }) {
    application {
        mainClass = "de.rubixdev.aoc.MainKt"
    }

    dependencies {
        implementation(project(":kotlin-shared"))
    }
}

configure(subprojects.filter { "scala" in it.name } + rootProject) {
    apply(plugin = "scala")

    scala {
        scalaVersion = "3.7.4"
    }

    dependencies {
        implementation("org.scala-lang:scala3-library_3:${scala.scalaVersion}")
    }
}

configure(subprojects.filter { "scala" in it.name && it.name != "scala-shared" }) {
    application {
        mainClass = "de.rubixdev.aoc.main"
    }

    dependencies {
        implementation(project(":scala-shared"))
    }
}
