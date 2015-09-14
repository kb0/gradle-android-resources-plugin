package com.kb.gradle.resources

import org.gradle.testfixtures.ProjectBuilder

class AndroidResourcesPluginExtensionTest extends GroovyTestCase {
    void testDefaultAdbPath() {
        def extension = createExtension()
        assert true
    }

    private static AndroidResourcesPluginExtension createExtension() {
        def projectDir = new File('..')
        def project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        def extension = new AndroidResourcesPluginExtension()
        extension
    }
}

