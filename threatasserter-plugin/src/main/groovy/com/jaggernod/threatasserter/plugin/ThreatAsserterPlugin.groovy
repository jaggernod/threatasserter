package com.jaggernod.threatasserter.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class ThreatAsserterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)
        if (!hasApp && !hasLib) {
            throw new IllegalStateException("'android' or 'android-library' plugin required.")
        }

        final def log = project.logger
        final def variants
        if (hasApp) {
            variants = project.android.applicationVariants
        } else {
            variants = project.android.libraryVariants
        }

        def version = '1.0.5'
        project.dependencies {
            debugCompile "com.jaggernod:threatasserter-runtime:$version"
            // TODO this should come transitively
            debugCompile 'org.aspectj:aspectjrt:1.8.6'
            compile "com.jaggernod:threatasserter-annotations:$version"
        }

        project.extensions.create('threatAsserter', ThreatAsserterExtension)

        variants.all { variant ->
            if (!project.threatAsserter.enabled) {
                log.info("Threat Asserter is not enabled.")
                return
            }
            if (variant.buildType.name == 'release') {
                log.info("Threat Asserter does not act for release builds.")
                return
            }

            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = [
                        "-showWeaveInfo",
                        "-1.5",
                        "-inpath", javaCompile.destinationDir.toString(),
                        "-aspectpath", javaCompile.classpath.asPath,
                        "-d", javaCompile.destinationDir.toString(),
                        "-classpath", javaCompile.classpath.asPath,
                        "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)
                ]
                log.debug "ajc args: " + Arrays.toString(args)

                weave(args, log)

                def kotlinClasses = project.file("${project.buildDir}/tmp/kotlin-classes/debug")
                if (kotlinClasses.exists()) {
                    String[] argsKotlin = [
                            "-showWeaveInfo",
                            "-1.5",
                            "-inpath", kotlinClasses.toString(),
                            "-aspectpath", javaCompile.classpath.asPath,
                            "-d", kotlinClasses.toString(),
                            "-classpath", javaCompile.classpath.asPath,
                            "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)
                    ]

                    log.debug "ajc args for Kotlin: " + Arrays.toString(args)

                    weave(argsKotlin, log)
                }
            }
        }
    }

    static void weave(args, log) {
        MessageHandler handler = new MessageHandler(true)
        new Main().run(args, handler)
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break
                case IMessage.WARNING:
                    log.warn message.message, message.thrown
                    break
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break
            }
        }
    }
}
