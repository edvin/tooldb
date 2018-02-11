package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.controllers.DB
import com.cyclestart.tooldb.models.ToolType
import com.cyclestart.tooldb.models.ToolTypeAdded
import com.cyclestart.tooldb.models.ToolTypeModel
import tornadofx.*

class ToolTypeTypeList : View("ToolTypes") {
    val db: DB by inject()
    val selectedToolType = ToolTypeModel()
    override val deletable = !selectedToolType.empty

    override val root = tableview<ToolType> {
        column("Name", ToolType::nameProperty).makeEditable()
        smartResize()
        onEditCommit {
            runAsync { db.updateToolType(it) }
        }
        subscribe<ToolTypeAdded> { onRefresh() }
        bindSelected(selectedToolType)
    }

    override fun onDelete() {
        confirm("Confirm tool type delete", "Do you want to delete ${selectedToolType.name.value}?") {
            runAsync {
                db.deleteToolType(selectedToolType.item)
            } ui {
                onRefresh()
            }
        }
    }

    override fun onDock() {
        onRefresh()
    }

    override fun onRefresh() {
        root.asyncItems { db.listToolTypes() }
    }

    override fun onCreate() {
        val toolType = ToolTypeModel(ToolType())

        dialog("Add ToolType") {
            form {
                fieldset {
                    field("Name") {
                        textfield(toolType.name).required()
                    }
                }
                button("Create") {
                    isDefaultButton = true

                    action {
                        toolType.commit {
                            runAsync {
                                db.insertToolType(toolType.item)
                            } ui {
                                close()
                            }
                        }
                    }
                }
            }
        }
    }

}