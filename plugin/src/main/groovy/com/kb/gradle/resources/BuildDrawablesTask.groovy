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
        def pngquantBinary = extension.getPngquantBinary(project);
        def jpgoptimBinary = extension.getJpgoptimBinary(project);

        logger.info("use imageMagick at - " + imageMagickBinary);

        // get drawables list
        def drawables = extension.getDrawables();
        println "process drawables - $drawables"

        drawables.each() { sourcePath, settings ->
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

            Map imSettings = [:];
            if (settings instanceof Map) {
                imSettings = settings;
            } else {
                imSettings = ["dimension": settings];
            }

            // @TODO iterate android flavours
            // @TODO get destination path from android project settings
            def drawableTarget = new File(getProject().getProjectDir().toString(), "\\src\\main\\res\\drawable-mdpi\\");
            if (imSettings.containsKey("target")) {
                drawableTarget = new File(getProject().getProjectDir().toString(), "\\src\\main\\res\\" + imSettings["target"] + "\\");
            }
            drawableTarget.mkdirs();

            sources.each() { source ->
                def dimension = imSettings["dimension"];
                String drawableName = FilenameUtils.getBaseName(source)
                String drawableExtension = imSettings.containsKey("extension") ? imSettings["extension"] : FilenameUtils.getExtension(source);
                String drawableFormat = imSettings.containsKey("format") ? imSettings["format"] + ":" : "";

                def targetPath = new File(drawableTarget, "${drawableName}_${dimension}.$drawableExtension");

                List imArgs = [source, "-antialias", "-resize", dimension, targetPath.getAbsolutePath()];
                if (imSettings.containsKey("args") && imSettings["args"] instanceof List) {
                    imArgs.clear();
                    // add custom settings
                    imArgs.addAll((List) imSettings["args"]);
                    // add source
                    imArgs.add(0, source);
                    // add target
                    imArgs.add(drawableFormat + targetPath.getAbsolutePath());
                }

                println "process $imageMagickBinary for $source into $targetPath"
                println imArgs
                getProject().exec {
                    workingDir project.projectDir
                    executable imageMagickBinary
                    args imArgs
                    ignoreExitValue true
                }

                if (imSettings.containsKey("optimize") && imSettings["optimize"]) {
                    if ("png".equals(drawableExtension.toLowerCase())) {
                        println "optimize $targetPath with pngquant"
                        getProject().exec {
                            workingDir project.projectDir
                            executable pngquantBinary
                            args "--force", "--verbose", "--ordered", "--speed=1", "--quality=25-90", "--ext", ".png", targetPath.getAbsolutePath()
                            ignoreExitValue true
                        }
                    }
                    if ("jpg".equals(drawableExtension.toLowerCase())) {
                        println "optimize $targetPath with jpgoptim"
                        getProject().exec {
                            workingDir project.projectDir
                            executable jpgoptimBinary
                            args "--strip-all", "-o", "-f", "-v", targetPath.getAbsolutePath()
                            ignoreExitValue true
                        }
                    }
                }
            }
        };
    }
}
