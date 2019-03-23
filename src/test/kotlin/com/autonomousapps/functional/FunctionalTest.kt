package com.autonomousapps.functional

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class FunctionalTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()

    private lateinit var settingsFile: File
    private lateinit var buildFile: File
    private lateinit var sourceDir: File

    @Before
    fun setup() {
        settingsFile = testProjectDir.newFile("settings.gradle.kts")
        buildFile = testProjectDir.newFile("build.gradle.kts")
        sourceDir = testProjectDir.newFolder("src", "main", "kotlin")
    }

    @Test
    fun `a data class is ok`() {
        settingsFile()
        buildFile()

        val className = "MyDataClass"
        kotlinSourceFile("$className.kt", """
            data class $className(val s: String)
        """.trimIndent())

        build("check").apply {
            assertTrue { task(":onlyDataClasses")?.outcome == TaskOutcome.SUCCESS }
        }
    }

    @Test
    fun `a non-data class is a big no-no`() {
        settingsFile()
        buildFile()

        val className = "MyNonDataClass"
        kotlinSourceFile("$className.kt", """
            class $className(val s: String)
        """.trimIndent())

        buildAndFail("check").apply {
            assertTrue { task(":onlyDataClasses")?.outcome == TaskOutcome.FAILED }
            assertTrue { output.contains("Project has non-data Kotlin classes") }
            assertTrue {
                val outputFile = File(testProjectDir.root, "build/onlyDataClasses/nonDataClasses.txt")
                outputFile.readText().contains("$className.kt")
            }
        }
    }

    private fun settingsFile() = settingsFile.apply {
        writeText("rootProject.name = \"only-data-classes\"")
    }

    private fun buildFile() = buildFile.apply {
        writeText("""
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.3.20"
                id("com.autonomousapps.onlydataclasses") version "0.1"
            }
            repositories {
                jcenter()
            }
        """.trimIndent())
    }

    private fun kotlinSourceFile(name: String, content: String) = File(sourceDir, name).apply {
        writeText(content)
    }

    private fun build(vararg args: String): BuildResult = gradleRunnerFor(*args).build()

    private fun buildAndFail(vararg args: String): BuildResult = gradleRunnerFor(*args).buildAndFail()

    private fun gradleRunnerFor(vararg args: String): GradleRunner {
        return GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments(args.toList() + "--stacktrace")
            .withPluginClasspath()
            .forwardOutput()
    }
}