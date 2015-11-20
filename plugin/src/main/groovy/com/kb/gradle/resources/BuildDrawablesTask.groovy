package com.kb.gradle.resources

import org.apache.commons.io.FileSystemUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files

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
        new File(drawableTarget).mkdirs();

        drawables.each() { sourcePath, dimensions ->
            def sources = new ArrayList<String>()
            if (sourcePath instanceof FileTree) {
                sourcePath.each {
                    sources << it.absolutePath
                }
            } else if (new File(sourcePath).isDirectory()) {
                getProject().fileTree(sourcePath).each {
                    sources << it.absolutePath
                }
            } else {
                sources << sourcePath
            }

            sources.each() { source ->
                dimensions.each() { dimension ->
                    def drawableName = FilenameUtils.getBaseName(source)
                    def drawableExtension = FilenameUtils.getExtension(source)

                    def targetPath = "$drawableTarget\\${drawableName}_${dimension}.$drawableExtension";
                    println "process $imageMagickBinary for $source into $targetPath"
                    getProject().exec {
                        workingDir project.projectDir
                        executable imageMagickBinary
                        args(
                                "${source}",
                                "-antialias", "-resize", "${dimension}",
                                targetPath
                        )
                        ignoreExitValue true
                    }
                };
            }
        };
    }
}
