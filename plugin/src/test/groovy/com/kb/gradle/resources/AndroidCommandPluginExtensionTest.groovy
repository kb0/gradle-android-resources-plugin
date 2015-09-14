package com.kb.gradle.resources

import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class AndroidResourcesPluginExtensionTest {
    @Test
    public void canCreateExtension() {
        // Project project = ProjectBuilder.builder().build()
        def extension = new AndroidResourcesPluginExtension()
        assert (extension instanceof AndroidResourcesPluginExtension)
    }

    @Test
    public void canAddBuildDrawablesTaskToProject() {
        Project project = ProjectBuilder.builder().build()

        def task = project.task('buildDrawables', type: BuildDrawablesTask)
        assertTrue(task instanceof BuildDrawablesTask)
    }

    @Test
    public void cannotApplyPluginToProjectWithoutAndroid() {
        Project project = ProjectBuilder.builder().build()

        try {
            project.pluginManager.apply('android-resources')
            assert false
        } catch (StopExecutionException ignored) {
            assert true
        }
        //assertTrue(task instanceof BuildDrawablesTask)
    }


}

