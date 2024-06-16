package org.demoth.cake.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.actors.onClick
import ktx.scene2d.actors
import ktx.scene2d.label
import ktx.scene2d.table
import ktx.scene2d.textButton

/**
 * Prototype for other stages
 */
class MainMenuStage(viewport: Viewport) : Stage(viewport) {
    init {
        actors {
            table {
                defaults().pad(8f)
                setFillParent(true)
                textButton("Single player")
                row()
                textButton("Multiplayer")
                row()
                textButton("Settings")
                row()
                textButton("Exit") {
                    onClick {
                        Gdx.app.exit()
                    }
                }
            }
            label("version: 0.0.1")
        }
    }
}
