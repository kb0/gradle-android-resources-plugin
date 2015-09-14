package com.kb.gradle.resources

import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

class AndroidResourcesPluginExtensionTest {
    @Test
    public void canCreateExtension() {
        // Project project = ProjectBuilder.builder().build()
        def extension = new AndroidResourcesPluginExtension()
        assert (extension instanceof AndroidResourcesPluginExtension)
    }

    @Test(expected = PluginApplicationException.class)
    public void cannotApplyPluginToProjectWithoutAndroid() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply('android-resources')
    }
}

