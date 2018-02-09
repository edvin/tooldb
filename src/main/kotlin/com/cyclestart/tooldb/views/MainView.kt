package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.app.Styles
import com.cyclestart.tooldb.controllers.DB
import tornadofx.*

class MainView : View("Hello TornadoFX") {
    val db: DB by inject()

    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
        add<VendorList>()
    }

    override fun onDock() {
        db.listVendors().forEach { println(it.name) }
    }
}