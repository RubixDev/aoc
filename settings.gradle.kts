plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "aoc"

include("kotlin-shared")
val projects = listOf("2015", "2024", "2025")
for (project in projects) {
    include(project)
    project(":$project").projectDir = file("$project/kotlin")
}
