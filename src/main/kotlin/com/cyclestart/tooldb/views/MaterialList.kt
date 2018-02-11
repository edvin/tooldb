package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.controllers.DB
import com.cyclestart.tooldb.models.Material
import com.cyclestart.tooldb.models.MaterialAdded
import com.cyclestart.tooldb.models.MaterialModel
import tornadofx.*

class MaterialList : View("Materials") {
    val db: DB by inject()
    val selectedMaterial = MaterialModel()
    override val deletable = !selectedMaterial.empty

    override val root = tableview<Material> {
        column("Name", Material::nameProperty).minWidth(300).makeEditable()
        column("Hardness", Material::hardnessProperty).makeEditable()
        smartResize()
        onEditCommit {
            runAsync { db.updateMaterial(it) }
        }
        subscribe<MaterialAdded> { onRefresh() }
        bindSelected(selectedMaterial)
    }

    override fun onDelete() {
        confirm("Confirm material delete", "Do you want to delete ${selectedMaterial.name.value}?") {
            runAsync {
                db.deleteMaterial(selectedMaterial.item)
            } ui {
                onRefresh()
            }
        }
    }

    override fun onDock() {
        onRefresh()
    }

    override fun onRefresh() {
        root.asyncItems { db.listMaterials() }
    }

    override fun onCreate() {
        val material = MaterialModel(Material())

        dialog("Add Material") {
            form {
                fieldset {
                    field("Name") {
                        textfield(material.name).required()
                    }
                    field("Hardness") {
                        textfield(material.hardness).required()
                    }
                }
                button("Create").action {
                    material.commit {
                        runAsync {
                            db.insertMaterial(material.item)
                        } ui {
                            close()
                        }
                    }
                }
            }
        }
    }

}