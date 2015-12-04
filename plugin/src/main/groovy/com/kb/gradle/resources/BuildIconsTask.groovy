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

        // default filter
        def iconsFilter = extension.getIconsFilter();
        if (iconsFilter == null || iconsFilter.isEmpty()) {
            iconsFilter = "Lanczos2"
        }

        // default resize
		def iconsResize = extension.getIconsResize();
        if (iconsResize == null || iconsResize.isEmpty()) {
            iconsResize = "-resize"
        }

        // @TODO iterate android flavours
        // @TODO get destination path from android project settings
        def drawableTarget = getProject().getProjectDir().toString() + "\\src\\main\\res\\drawable-";


        icons.each() { iconName, iconSource ->
            def iconSourceFile = new File(iconSource)

            extension.getDensities().each() { density ->
                new File(drawableTarget + density).mkdirs();
                def targetPath = "$drawableTarget$density\\${extension.getLauncherPrefix()}$iconName" + ".png";
                Integer targetSize = Math.round(extension.getDpiRation().get(density) * extension.getLauncherIcon()) - Math.round(extension.getDpiRation().get(density) * extension.getLauncherIconBorder()) * 2
                Integer borderSize = Math.round(extension.getDpiRation().get(density) * extension.getLauncherIcon())

                println "process $imageMagickBinary for $iconSource into $targetPath"
                getProject().exec {
                    workingDir project.projectDir
                    executable imageMagickBinary
                    args(
                            "-background", "transparent", "${iconSource}", "-strip", "-trim", "+repage",
                            "-antialias", "-filter", iconsFilter, iconsResize, "${targetSize}x${targetSize}", "-gravity", "center", "-extent", "${borderSize}x${borderSize}",
                            targetPath
                    )
                    ignoreExitValue true
                }

            };

            extension.getDensities().each() { density ->
                new File(drawableTarget + density).mkdirs();
                def targetPath = "$drawableTarget$density\\${extension.getNavPrefix()}$iconName" + ".png";
                Integer targetSize = Math.round(extension.getDpiRation().get(density) * extension.getNavIcon())

                println "process $imageMagickBinary for $iconSource into $targetPath"
                getProject().exec {
                    workingDir project.projectDir
                    executable imageMagickBinary
                    args(
                            "-background", "transparent", "${iconSource}", "-strip", "-trim", "+repage",
                            "-antialias", "-filter", iconsFilter, iconsResize, "${targetSize}x${targetSize}", "-gravity", "center", "-extent", "${targetSize}x${targetSize}",
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
                        "-antialias", "-filter", iconsFilter, iconsResize, "512x512", "-gravity", "center", "-extent", "512x512",
                        targetPath
                )
                ignoreExitValue true
            }
        };
    }
}
