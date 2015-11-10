# gradle-android-resources-plugin
[![Build Status](https://travis-ci.org/x2on/gradle-spoon-plugin.png)](https://travis-ci.org/x2on/gradle-spoon-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/de.felixschulze.gradle/gradle-spoon-plugin.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22de.felixschulze.gradle%22%20AND%20a%3A%22gradle-spoon-plugin%22)
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

* `teamCityLog`: Add features for [TeamCity](http://www.jetbrains.com/teamcity/)
* `debug`: Activate debug output for spoon
* `failOnFailure`: Deactivate exit code on failure
* `testSizes`: Only run test methods annotated by testSize (small, medium, large)
* `adbTimeout`: ADB timeout in seconds
* `failIfNoDeviceConnected`: Fail if no device is connected
* `excludedDevices`: List of devices which should be excluded

## Changelog

## License