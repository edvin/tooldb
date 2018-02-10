package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.app.Styles
import com.cyclestart.tooldb.controllers.DB
import tornadofx.*

class MainView : View("Cutting Tool Database") {
    val db: DB by inject()

    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
    }
}