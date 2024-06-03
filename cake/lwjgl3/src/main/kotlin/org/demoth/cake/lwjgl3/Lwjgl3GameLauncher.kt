package org.demoth.cake.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import org.demoth.cake.Cake

/** Launches the desktop (LWJGL3) application. */
object Lwjgl3GameLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        if (startNewJvmIfRequired()) return
        createApplication()
    }

    private fun createApplication(): Lwjgl3Application {
        return Lwjgl3Application(Cake(), getDefaultConfiguration())
    }

    private fun getDefaultConfiguration(): Lwjgl3ApplicationConfiguration {
        return Lwjgl3ApplicationConfiguration().apply {
            setTitle("cake-engine")

            // todo: load displaymode from the configuration
            useVsync(true)
            setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate)
            setResizable(false)
            setWindowedMode(640, 480)
//            setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode())
            setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        }
    }
}