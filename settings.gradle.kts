plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "aoc"

include("kotlin-shared")
include("scala-shared")
val projects = listOf("2015/kotlin", "2024/kotlin", "2025/kotlin", "2025/scala")
for (project in projects) {
    val projectName = project.replace('/', '-')
    include(projectName)
    project(":$projectName").projectDir = file(project)
}
