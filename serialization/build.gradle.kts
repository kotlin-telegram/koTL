plugins {
    id("kmp-library-convention")
    id("publication-convention")
}

version = libs.versions.koTLVersion.get()

dependencies {
    commonMainApi(projects.core)
    commonMainImplementation(projects.libs.stdlibExtensions)
    commonMainImplementation(libs.kotlinxSerialization)
}
