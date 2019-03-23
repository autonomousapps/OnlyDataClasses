package com.autonomousapps.tasks

import org.gradle.api.GradleException
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.*
import java.io.File

@CacheableTask
open class OnlyDataClassesTask : SourceTask() {

    @InputFiles
    @PathSensitive(PathSensitivity.NONE)
    override fun getSource(): FileTree = super.getSource()

    @OutputFile
    fun getNoDataClassesFile(): File = project.file("${project.buildDir}/$name/nonDataClasses.txt")

    @TaskAction
    fun action() {
        var hasErrors = false

        source.forEach {
            // TODO This is fragile regex.
            if (it.readText().contains("(?<!data )class".toRegex())) {
                hasErrors = true
                getNoDataClassesFile().appendText(it.toRelativeString(project.projectDir))
            }
        }

        if (hasErrors) {
            throw GradleException("Project has non-data Kotlin classes! See ${getNoDataClassesFile()}")
        }
    }
}
