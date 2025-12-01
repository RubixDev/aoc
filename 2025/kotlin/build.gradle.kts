import com.diffplug.gradle.spotless.BaseKotlinExtension

plugins {
    kotlin("jvm") version "2.0.21"
    application
    id("com.diffplug.spotless") version "8.1.0"
}

group = "de.rubixdev"
version = "1.0-SNAPSHOT"

application {
    mainClass = "de.rubixdev.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

spotless {
    fun BaseKotlinExtension.customKtlint() = ktlint("1.8.0").editorConfigOverride(
        mapOf(
            "max_line_length" to 100,
        ),
    )
    kotlin {
        customKtlint()
    }
    kotlinGradle {
        customKtlint()
    }
}
