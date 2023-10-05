plugins {
    id("kmp-library-convention")
    id("publication-convention")
    id("print-sdk-version-convention")
}

version = libs.versions.koTLVersion.get()

dependencies {
    commonMainImplementation(libs.kotlinxSerialization)
}
