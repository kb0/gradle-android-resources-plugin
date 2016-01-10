package com.kb.gradle.resources
import org.gradle.api.Project

public class AndroidResourcesPluginExtension {
    // Google Store assets
    public Map<String, Integer> webAssets = new HashMap<String, Integer>() {
        {
            put("web", 512);
        }
    };

    def List<String> densities = ["mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"];


    // Launcher
    String launcherPrefix = "ic_launcher_";
    int launcherIcon = 48;
    int launcherIconBorder = 1;

    // Navigation
    String navPrefix = "ic_nav_";
    int navIcon = 24;


    def Map<String, Double> dpiRation = new LinkedHashMap<String, Double>() {
        {
            put("xxxhdpi", 4.0);
            put("xxhdpi", 3.0);
            put("xhdpi", 2.0);
            put("hdpi", 1.5);
            put("mdpi", 1.0);
        }
    };

    def icons
    def iconsFilter
    def iconsResize

    def drawables

    static String getImageMagickBinary(Project project) {
        if (project.ext.has("imageMagickBinary")) {
            return project.ext.imageMagickBinary
        }

        return "convert"
    }

    static String getPngquantBinary(Project project) {
        if (project.ext.has("pngquantBinary")) {
            return project.ext.pngquantBinary
        }

        return "pngquant"
    }

    static String getJpgoptimBinary(Project project) {
        if (project.ext.has("jpgoptim")) {
            return project.ext.jpgoptim
        }

        return "jpgoptim"
    }

    def translationMap
    def String translationSource

    def String translationTarget = "strings_exported.xml";
    def String translationDefaultLocale = "en";
}
