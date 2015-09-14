package com.kb.gradle.resources

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException

public class AndroidResourcesPlugin implements Plugin<Project> {
    void apply(Project project) {
        if (!project.plugins.hasPlugin('com.android.application') && !project.plugins.hasPlugin('com.android.library')) {
            throw new StopExecutionException("The 'android' plugin is required.")
        }

        project.getExtensions().create("androidResources", AndroidResourcesPluginExtension.class);
        project.getTasks().create("buildDrawables", BuildDrawablesTask.class);
    }
}
