package com.kb.gradle.resources;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction;

public class BuildIconsTask extends DefaultTask {

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
        def icons = extension.getIcons();
        println "process drawables - $icons"

        // @TODO iterate android flavours
        // @TODO get destination path from android project settings
        def drawableTarget = getProject().getProjectDir().toString() + "\\src\\main\\res\\drawable-";


        icons.each() { iconName, iconSource ->
            def iconSourceFile = new File(iconSource)

            extension.getLauncherAssets().each() { key, size ->
                new File(drawableTarget + key).mkdirs();
                def targetPath = "$drawableTarget$key\\${extension.getLauncherPrefix()}$iconName" + ".png";
                println "process $imageMagickBinary for $iconSource into $targetPath"
                getProject().exec {
                    workingDir project.projectDir
                    executable imageMagickBinary
                    args(
                            "-background", "transparent", "${iconSource}", "-strip", "-trim", "+repage",
                            "-antialias", "-resize", "${size}x${size}", "-gravity", "center", "-extent", "${size}x${size}",
                            targetPath
                    )
                    ignoreExitValue true
                }

            };

            extension.getActionbarAssets().each() { key, size ->
                new File(drawableTarget + key).mkdirs();
                def targetPath = "$drawableTarget$key\\${extension.getActionbarPrefix()}$iconName" + ".png";
                println "process $imageMagickBinary for $iconSource into $targetPath"
                getProject().exec {
                    workingDir project.projectDir
                    executable imageMagickBinary
                    args(
                            "-background", "transparent", "${iconSource}", "-strip", "-trim", "+repage",
                            "-antialias", "-resize", "${size}x${size}", "-gravity", "center", "-extent", "${size}x${size}",
                            targetPath
                    )
                    ignoreExitValue true
                }
            };

            def targetPath = new File(iconSourceFile.getParentFile().getAbsolutePath(), "${iconName}_web.png" );
            println "process $imageMagickBinary for $iconSource into $targetPath"
            getProject().exec {
                workingDir project.projectDir
                executable imageMagickBinary
                args(
                        "-background", "transparent", "${iconSource}", "-strip", "-trim", "+repage",
                        "-antialias", "-resize", "512x512", "-gravity", "center", "-extent", "512x512",
                        targetPath
                )
                ignoreExitValue true
            }
        };
    }
}
