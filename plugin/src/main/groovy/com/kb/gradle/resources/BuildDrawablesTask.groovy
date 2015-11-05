package com.kb.gradle.resources

import org.apache.commons.io.FilenameUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by kb on 07.10.2015.
 */
class BuildDrawablesTask extends DefaultTask {

    @TaskAction
    public void action() {
        AndroidResourcesPluginExtension extension = getProject().getExtensions().findByType(AndroidResourcesPluginExtension.class);
        if (extension == null) {
            extension = new AndroidResourcesPluginExtension();
        }

        // check ImageMagick binary
        def imageMagickBinary = extension.getImageMagickBinary(project);
        logger.info("use imageMagick at - " + imageMagickBinary);

        // get drawables list
        def drawables = extension.getDrawables();
        println "process drawables - $drawables"

        // @TODO iterate android flavours
        // @TODO get destination path from android project settings
        def drawableTarget = getProject().getProjectDir().toString() + "\\src\\main\\res\\drawable-mdpi\\";
        drawables.each() { source, dimensions ->
            dimensions.each() { dimension ->
                // create destination directory
                new File(drawableTarget).mkdirs();

                def drawableName = FilenameUtils.getBaseName(source)
                def drawableExtension = FilenameUtils.getExtension(source)

                def targetPath = "$drawableTarget\\${drawableName}_${dimension}.$drawableExtension";
                println "process $imageMagickBinary for $iconSource into $targetPath"
                getProject().exec {
                    workingDir project.projectDir
                    executable imageMagickBinary
                    args(
                            "${source}",
                            "-antialias", "-resize", "${dimension[0]}x${dimension[1]}",
                            targetPath
                    )
                }

            };
        };
    }
}
