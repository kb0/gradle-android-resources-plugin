# gradle-android-resources-plugin
A Gradle plugin for manage Android resource.

## Basic usage

Add to your build.gradle

```gradle
buildscript {
    repositories {
        maven {
            url "http://dl.bintray.com/kb0/maven"
        }
    }
    classpath 'com.kb.gradle:gradle-android-resources-plugin:2.0.4'
}

apply plugin: 'gradle-android-resources-plugin'
```

Add to your build.gradle

```gradle
androidResources {
    drawables = [
            "social_facebook"  : "d:\\__DESIGN\\social\\social-facebook.png",
            "social_googleplus": "d:\\__DESIGN\\social\\social-googleplus.png",
            "social_twitter"   : "d:\\__DESIGN\\social\\social-twitter.png",
    ];

    translationMap = [
            'text_settings'       : ['Gmail2menu_settings', 'Velvetmenu_settings', 'Hangoutsbabel_settings_label', 'WordPressmy_site_btn_site_settings'],
    ];
```

Run tasks
..

## Changelog

## License