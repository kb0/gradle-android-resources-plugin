package com.kb.gradle.resources

import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.api.tasks.StopExecutionException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ExtractStringsTaskTest {
    Project project;

    @Test
    public void canAddBuildExtractStringsTaskToProject() {
        def task = getTask()
        Assert.assertTrue(task instanceof ExtractStringsTask)
    }

    @Test(expected = StopExecutionException.class)
    public void cannotRunExtractStringsWithoutTranslationMap() {
        // run task
        getTask().action();
    }

    @Test(expected = StopExecutionException.class)
    public void cannotRunExtractStringsWithoutSource() {
        project.extensions.getByName("androidResources").translationMap = ["text_test" : []];

        // run task
        getTask().action();
    }

    @Test
    public void canRunExtractStrings() {
        project.extensions.getByName("androidResources").translationMap = ["text_test" : ['test1', 'test2']];
        project.extensions.getByName("androidResources").translationSource = new File(System.properties.fixtures, "strings").toString();

        // run task
        getTask().action();
    }

    @Before
    public void onSetup() {
        project = ProjectBuilder.builder().build()
        project.getExtensions().create("androidResources", AndroidResourcesPluginExtension.class);
    }

    @After
    public void onEnd() {
        project = null;
    }

    public ExtractStringsTask getTask() {
        return project.task('extractStrings', type: ExtractStringsTask)
    }
}