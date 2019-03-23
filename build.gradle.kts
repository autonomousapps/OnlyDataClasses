plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
    id("com.gradle.plugin-publish") version "0.10.0"
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

//    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin")
    
    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

// Required to put the Kotlin plugin on the classpath for the functional test suite
//tasks.withType<PluginUnderTestMetadata>().configureEach {
//    pluginClasspath.from(configurations.compileOnly)
//}

version = "0.1"
group = "com.autonomousapps.onlydataclasses"

pluginBundle {
    website = "https://github.com/autonomousapps/OnlyDataClasses"
    vcsUrl = "https://github.com/autonomousapps/OnlyDataClasses"
    tags = listOf("kotlin")
}

gradlePlugin {
    plugins {
        create("onlyDataClasses") {
            id = "com.autonomousapps.onlydataclasses"
            displayName = "Only Data Classes Allowed Plugin"
            description = "A silly plugin that will complain if you add a non-\"data class\" Kotlin class"
            implementationClass = "com.autonomousapps.plugin.OnlyDataClassesPlugin"
        }
    }
}
