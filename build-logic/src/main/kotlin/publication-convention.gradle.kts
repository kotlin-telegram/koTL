plugins {
    id("org.gradle.maven-publish")
}

group = "me.y9san9.kotl"

publishing {
    repositories {
        maven {
            name = "koTL"
            url = uri("https://maven.pkg.github.com/kotlin-telegram/kotl")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
