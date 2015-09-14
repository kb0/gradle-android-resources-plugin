package com.kb.gradle.resources

import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

class BuildDrawablesTaskTest {
    @Test
    public void canAddBuildDrawablesTaskToProject() {
        Project project = ProjectBuilder.builder().build()

        def task = project.task('buildDrawables', type: BuildDrawablesTask)
        Assert.assertTrue(task instanceof BuildDrawablesTask)
    }

    @Test
    public void canRunBuildDrawablesTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.getExtensions().create("androidResources", AndroidResourcesPluginExtension.class);

        project.ext.imageMagickBinary = System.properties.imageMagickBinary
        project.extensions.getByName("androidResources").drawables = ["icon" : new File(System.properties.fixtures, "icon.svg").toString()];

        def task = project.task('buildDrawables', type: BuildDrawablesTask)
        Assert.assertTrue(task instanceof BuildDrawablesTask)

        // run task
        task.action();
    }
}