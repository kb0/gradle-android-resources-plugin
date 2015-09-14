package com.kb.gradle.resources
import org.gradle.api.Project

public class AndroidResourcesPluginExtension {

    def seed

    def getSeed() {
        System.properties['seed'] ?: seed
    }
}
