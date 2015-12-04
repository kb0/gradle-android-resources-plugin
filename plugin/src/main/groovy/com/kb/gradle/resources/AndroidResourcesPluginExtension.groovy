package com.kb.gradle.resources
import org.gradle.api.Project

public class AndroidResourcesPluginExtension {
    // Google Store assets
    public Map<String, Integer> webAssets = new HashMap<String, Integer>() {
        {
            put("web", 512);
        }
    };

    // Launcher
    String launcherPrefix = "ic_launcher_";
    def Map<String, Integer> launcherAssets = new LinkedHashMap<String, Integer>() {
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
    def Map<String, Integer> actionbarAssets = new LinkedHashMap<String, Integer>() {
        {
            put("xxxhdpi", 160);
            put("xxhdpi", 120);
            put("xhdpi", 80);
            put("hdpi", 60);
            put("mdpi", 40);
        }
    };

    def Map<String, Double> dpiRation = new LinkedHashMap<String, Double>() {
        {
            put("xxxhdpi", 192.0 / 48.0);
            put("xxhdpi", 144.0 / 48.0);
            put("xhdpi", 96.0 / 48.0);
            put("hdpi", 72.0 / 48.0);
            put("mdpi", 48.0 / 48.0);
        }
    };

    def icons
    def iconsFilter

    def drawables

    def getImageMagickBinary(Project project) {
        if (project.ext.has("imageMagickBinary")) {
            return project.ext.imageMagickBinary
        }

        return "convert"
    }

    def translationMap
    def String translationSource

    def String translationTarget = "strings_exported.xml";
    def String translationDefaultLocale = "en";
}
