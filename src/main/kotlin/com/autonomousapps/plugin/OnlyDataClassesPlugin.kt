package com.autonomousapps.plugin

import com.autonomousapps.tasks.OnlyDataClassesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class OnlyDataClassesPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = target.run {
        val onlyDataClasses = tasks.register("onlyDataClasses", OnlyDataClassesTask::class.java) {
            source = files("src/main/kotlin").asFileTree
        }
        tasks.named("check").configure {
            dependsOn(onlyDataClasses)
        }
    }

}
