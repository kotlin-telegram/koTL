plugins {
    id("kmp-library-convention")
    id("publication-convention")
}

version = libs.versions.koTLVersion.get()

dependencies {
    commonMainImplementation(projects.libs.stringParser)
}
