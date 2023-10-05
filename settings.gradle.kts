enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

includeBuild("build-logic")

include(
    ":core",
    ":serialization",
    ":schema",
    ":libs:stdlib-extensions",
    ":libs:string-parser",
)

rootProject.name = "kotl"
