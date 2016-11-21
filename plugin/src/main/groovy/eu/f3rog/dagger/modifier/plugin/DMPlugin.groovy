package eu.f3rog.dagger.modifier.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin
 *
 * @author FrantisekGazo
 */
public final class DMPlugin
        implements Plugin<Project> {

    public static String ERROR_GRADLE_TOOLS_1_5_0_REQUIRED = "Blade plugin only supports android gradle plugin 1.5.0 or later."
    public static String ERROR_ANDROID_PLUGIN_REQUIRED = "'com.android.application' or 'com.android.library' plugin required."

    @Override
    void apply(Project project) {
        // Make sure the project is either an Android application or library
        boolean isAndroidApp = project.plugins.withType(AppPlugin)
        boolean isAndroidLib = project.plugins.withType(LibraryPlugin)
        if (!isAndroidApp && !isAndroidLib) {
            throw new GradleException(ERROR_ANDROID_PLUGIN_REQUIRED)
        }

        // check gradle plugin
        if (!isTransformAvailable()) {
            throw new GradleException(ERROR_GRADLE_TOOLS_1_5_0_REQUIRED)
        }

        // apply bytecode weaving via Transform API
        project.android.registerTransform(new DMTransformer(true))
    }

    private static boolean isTransformAvailable() {
        try {
            Class.forName('com.android.build.api.transform.Transform')
            return true
        } catch (Exception ignored) {
            return false
        }
    }
}
