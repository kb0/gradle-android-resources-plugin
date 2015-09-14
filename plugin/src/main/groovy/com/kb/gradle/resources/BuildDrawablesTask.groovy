package com.kb.gradle.resources;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class BuildDrawablesTask extends DefaultTask {
    @TaskAction
    public void greet() {
        AndroidResourcesPluginExtension extension = getProject().getExtensions().findByType(AndroidResourcesPluginExtension.class);
        if (extension == null) {
            extension = new AndroidResourcesPluginExtension();
        }

        def drawableSource = extension.getDrawablesSource();
        // @TODO iterate android flavours
        // @TODO get destination path from android project settings
        def drawableTarget = getProject().getProjectDir().toString() + "\\src\\main\\res\\drawable-";

        // Google Store assets
        Map<String, Integer> webAssets = new HashMap<String, Integer>() {
            {
                put("web", 512);
            }
        };

        // Launcher
        String launcherPrefix = "ic_launcher_";
        Map<String, Integer> launcherAssets = new LinkedHashMap<String, Integer>() {
            {
                put("xxxhdpi", 192);
                put("xxhdpi", 144);
                put("xhdpi", 96);
                put("hdpi", 72);
                put("mdpi", 48);
            }
        };

        // Menu
        String actionbarPrefix = "ic_actionbar_";
        Map<String, Integer> actionbarAssets = new LinkedHashMap<String, Integer>() {
            {
                put("xxxhdpi", 160);
                put("xxhdpi", 120);
                put("xhdpi", 80);
                put("hdpi", 60);
                put("mdpi", 40);
            }
        };

        Map<String, Double> dpiRation = new LinkedHashMap<String, Double>() {
            {
                put("xxxhdpi", 192.0 / 48.0);
                put("xxhdpi", 144.0 / 48.0);
                put("xhdpi", 96.0 / 48.0);
                put("hdpi", 72.0 / 48.0);
                put("mdpi", 48.0 / 48.0);
            }
        };

        def icons = extension.getDrawables();

        icons.each() { iconName, iconSource ->
            launcherAssets.each() { key, size ->
                new File(drawableTarget + key).mkdirs();
                def targetPath = drawableTarget + key + "\\" + launcherPrefix + iconName + ".png";
                println targetPath
                exec {
                    workingDir project.projectDir
                    executable imageMagickBinary
                    args(
                            "-background", "transparent", "${drawableSource}${iconSource}", "-strip", "-trim", "+repage",
                            "-antialias", "-resize", "${size}x${size}", "-gravity", "center", "-extent", "${size}x${size}",
                            targetPath
                    )
                }

            };

            actionbarAssets.each() { key, size ->
                new File(drawableTarget + key).mkdirs();
                def targetPath = drawableTarget + key + "\\" + actionbarPrefix + iconName + ".png";
                println targetPath
                exec {
                    workingDir project.projectDir
                    executable imageMagickBinary
                    args(
                            "-background", "transparent", "${drawableSource}${iconSource}", "-strip", "-trim", "+repage",
                            "-antialias", "-resize", "${size}x${size}", "-gravity", "center", "-extent", "${size}x${size}",
                            targetPath
                    )
                }
            };
        };
    }
}