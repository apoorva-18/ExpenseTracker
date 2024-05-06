pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url=uri ("https://androidx.dev/storage/compose-compiler/repository/")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "ExpenseTracker"
include(":app")
