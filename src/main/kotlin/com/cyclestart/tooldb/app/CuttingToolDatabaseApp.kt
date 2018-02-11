package com.cyclestart.tooldb.app

import com.cyclestart.tooldb.controllers.DBActiveIndicator
import com.cyclestart.tooldb.views.MaterialList
import com.cyclestart.tooldb.views.MillingProfileList
import com.cyclestart.tooldb.views.ToolTypeTypeList
import com.cyclestart.tooldb.views.VendorList
import tornadofx.*

class CuttingToolWorkspace : Workspace() {
    init {
        navigationMode = NavigationMode.Tabs

        root.setPrefSize(800.0, 600.0)

        menubar {
            menu("Settings") {
                item("Vendors").action { dock<VendorList>() }
                item("Tool Types").action { dock<ToolTypeTypeList>() }
                item("Materials").action { dock<MaterialList>() }
                item("Milling Profiles").action { dock<MillingProfileList>() }
            }
        }
    }

    override fun onDock() {
        add<DBActiveIndicator>()
    }
}

class CuttingToolDatabaseApp : App(CuttingToolWorkspace::class, Styles::class)

