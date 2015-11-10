package com.kb.gradle.resources

import groovy.util.slurpersupport.GPathResult
import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction;
import groovy.xml.*;

public class ExtractStringsTask extends DefaultTask {

    public GPathResult getXmlDocument(File destinationXml) {
        if (!destinationXml.exists()) {
            println "create destination " + destinationXml
            destinationXml.getParentFile().mkdirs();

            def writer = new StringWriter()
            def xml = new MarkupBuilder(writer)

            xml.mkp.xmlDeclaration(version: "1.0", encoding: "utf-8")
            xml.resources()

            destinationXml << writer.toString()
        }

        return new XmlSlurper().parse(destinationXml)
    }

    @TaskAction
    public void action() {
        AndroidResourcesPluginExtension extension = getProject().getExtensions().findByType(AndroidResourcesPluginExtension.class);
        if (extension == null) {
            extension = new AndroidResourcesPluginExtension();
        }

        // get strings list
        def stringsMap = extension.getTranslationMap();
        if (stringsMap == null) {
            throw new StopExecutionException("'translationMap' should be set.")
        }

        def sourceBase = extension.getTranslationSource();
        if (sourceBase == null) {
            throw new StopExecutionException("Directory with translations (resources) should be set.")
        }
        if (!new File(sourceBase).isDirectory()) {
            throw new StopExecutionException("Directory with translations (" + sourceBase + ") should exists.")
        }

        def targetBase = new File(project.projectDir, "\\src\\main\\res")

        println "process strings - $stringsMap"

        new File(sourceBase).eachDir() { appResDir ->
            // process resources directories
            // "ast" (Asturian; Bable; Leonese; Asturleonese)
            // "bn-BD" (Bengali: Bangladesh)
            // "cy" (Welsh)
            // "eu" (Basque), "eu-ES" (Basque: Spain)
            // "gd" (Scottish Gaelic)
            // "gl" (Galician)
            // "gl-ES" (Galician: Spain)
            // "km" (Khmer),
            // "mk-MK" (Macedonian: Macedonia)
            // "ml-IN" (Malayalam: India),
            // "my-MM" (Burmese: Myanmar (Burma)),
            // "te-IN" (Telugu: India)
            if (appResDir.getName().startsWith("values") && !["values-ast", "values-bn-rBD", "values-cy", "values-eu", "values-eu-rES", "values-gd", "values-gl", "values-gl-rES", "values-km", "values-km-rKH", "values-mk-rMK", "values-ml-rIN", "values-my-rMM", "values-te-rIN", "values-zh", "values-he", "values-id", "values-ar-rXB", "values-en-rXA"].contains(appResDir.getName())) {
                appResDir.eachFile() { appResFile ->
                    // @TODO "arrays.xml", "plurals.xml"
                    if (["strings.xml"].contains(appResFile.getName()) && !appResFile.getName().contains("-land")) {
                        println "process resource " + appResFile.getPath();

                        // strip suffixes and create destination directory
                        def distDir = new File(targetBase, appResDir.getName().replaceAll("-(sw\\d+dp|v\\d+|land|xlarge|large|notouch)", ""))
                        distDir.mkdirs();

                        // create destination xml
                        def distFile = new File(distDir, extension.getTranslationTarget())

                        // xml
                        def xmlSource = new XmlSlurper().parse(appResFile);
                        def xmlValues = null;
                        def xmlValuesUpdate = false

                        stringsMap.each { key, valueArray ->
                            println "process string $key"
                            valueArray.any { value ->
                                println "process source key $value"

                                def sourceNode = xmlSource.string.find { it.@name == value }
                                if (sourceNode != null && sourceNode.@name == value) {
                                    if (xmlValues == null) {
                                        xmlValues = getXmlDocument(distFile);
                                    }

                                    def stringNode = null

                                    if (xmlValues.string != null) {
                                        stringNode = xmlValues.string.find {
                                            it.@name == key
                                        }
                                    }

                                    if (stringNode == null || stringNode.@name != key) {

                                        xmlValues.appendNode {
                                            string(name: key, sourceNode[0].text())
                                        }

                                        println 'save ' + key
                                    } else {

                                        println stringNode.text()
                                        println 'replace ' + key + " to " + sourceNode[0].text()
                                        stringNode.replaceBody sourceNode[0].text()
                                    }

                                    xmlValuesUpdate = true;
                                    return true
                                }
                            }
                        }

                        // save results
                        if (xmlValues != null && xmlValuesUpdate) {
                            XmlUtil.serialize(xmlValues, new FileWriter(distFile))
                        }
                    }
                }
            }
        }

        // copy default locale
        File defaultTranslationSource = new File(targetBase, "/values-" + extension.getTranslationDefaultLocale() + "/" + extension.getTranslationTarget());
        File defaultTranslationTarget = new File(targetBase, "/values/" + extension.getTranslationTarget())
        if (defaultTranslationSource.exists()) {
            FileUtils.copyFile(defaultTranslationSource, defaultTranslationTarget)
        }
    }
}
