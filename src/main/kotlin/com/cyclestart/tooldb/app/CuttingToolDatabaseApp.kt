package com.cyclestart.tooldb.app

import com.cyclestart.tooldb.controllers.DBActiveIndicator
import com.cyclestart.tooldb.views.VendorList
import tornadofx.*

class CuttingToolWorkspace : Workspace() {
    init {
        navigationMode = NavigationMode.Tabs

        root.setPrefSize(800.0, 600.0)

        menubar {
            menu("Settings") {
                item("Vendors").action { dock<VendorList>() }
            }
        }
    }

    override fun onDock() {
        add<DBActiveIndicator>()
    }
}

class CuttingToolDatabaseApp : App(CuttingToolWorkspace::class, Styles::class)

