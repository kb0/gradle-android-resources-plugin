package com.kb.gradle.resources
import org.gradle.api.Project

public class AndroidResourcesPluginExtension {

    def drawables

    def getDrawables() {
        return drawables
    }

    def getImageMagickBinary(Project project) {
        if (project.ext.has("imageMagickBinary")) {
            return project.ext.imageMagickBinary
        }

        return "convert"
    }
}
