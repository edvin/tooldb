package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.controllers.DB
import com.cyclestart.tooldb.models.*
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*

class MillingProfileEditor : View("Edit milling profile") {
    val profile: MillingProfileModel by inject()
    val db: DB by inject()

    override val savable = profile.dirty

    override val root = borderpane {
        top {
            form {
                fieldset {
                    field("Name") {
                        textfield(profile.name)
                    }
                }
            }
        }
    }

    override fun onDock() {
        onRefresh()
    }

    init {
        subscribe<MillingProfileEntryAdded>() { onRefresh() }
        subscribe<FzAdded>() { onRefresh() }
    }

    override fun onRefresh() {
        runAsync {
            val entries = db.listEntriesForMillingProfile(profile.item)
            val diameters = entries.flatMap { it.fz }.map { it.diameter }.distinct()
            diameters to entries
        } ui { (diameters, entries) ->
            createTable(diameters, entries)
        }
    }

    private fun createTable(diameters: List<Double>, entries: ObservableList<MillingProfileEntry>) {
        root.center = tableview(entries) {
            column("Material", MillingProfileEntry::materialProperty).remainingWidth()
            column<MillingProfileEntry, String>("Hardness", { it.value.materialProperty.value.hardnessProperty })
            column("Operation", MillingProfileEntry::operationProperty)
            column("aeMax", MillingProfileEntry::aeMaxProperty).makeEditable()
            column("apMax", MillingProfileEntry::apMaxProperty).makeEditable()
            column("Vc", MillingProfileEntry::vcProperty).makeEditable()

            val fzParent = nestedColumn("fz (mm/z) with nom. Ø") {
                diameters.forEach { diameter ->
                    column<MillingProfileEntry, Number>(diameter.toString(), {
                        // Locate the fz value for this diameter or set to a blank property with an Fz object so we can locate it on save
                        it.value.fz.find { it.diameter == diameter }?.fzProperty
                                ?: SimpleDoubleProperty(Fz().apply { millingProfileEntry = profile.item.id }, "fz")
                    }).minWidth(45).makeEditable()
                }
            }
            enableCellEditing()
            onEditCommit {
                // Save fz entry or the profile
                val firstSelectedColumn = selectionModel.selectedCells.first().tableColumn
                if (firstSelectedColumn.parentColumn == fzParent) {
                    // Find the property for the selected column and get to the bean, meaning the actual Fz object
                    val fzProperty = firstSelectedColumn.getTableColumnProperty(rowValue) as DoubleProperty
                    val fz = fzProperty.bean as Fz
                    runAsync {
                        db.saveFz(fz)
                    }
                } else {
                    selectedItem?.let {
                        runAsync {
                            db.updateMillingProfileEntry(it)
                        }
                    }
                }
            }
            contextmenu {
                item("Add fz") {
                    enableWhen(selectionModel.selectedItemProperty().isNotNull)
                    action {
                        selectedItem?.let { quickAddFz(it) }
                    }
                }
            }
            smartResize()
        }
    }

    private fun quickAddFz(entry: MillingProfileEntry) {
        val quickModel = object : ViewModel() {
            val diameters = bind { SimpleStringProperty(config.string("diametersDefault", "")) }
            val fzs = bind { SimpleStringProperty() }
        }

        dialog("Quick add fz") {
            form {
                label("Enter a space separated list of diameters followed by a space separated list of corresponding fz values")
                fieldset {
                    field("Diameters") {
                        textfield(quickModel.diameters).required()
                    }
                    field("Fz values") {
                        textfield(quickModel.fzs).required()
                    }
                }
                button("Add") {
                    isDefaultButton = true

                    action {
                        quickModel.commit {
                            if (quickModel.diameters.value.isBlank()) {
                                warning("Input error", "Enter at least one pair and try again")
                            } else {
                                addMultiFz(entry, quickModel.diameters.value, quickModel.fzs.value)
                                close()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addMultiFz(entry: MillingProfileEntry, diametersString: String, fzsString: String) {
        val diameters = diametersString.split(" ").map { it.trim().toDouble() }
        val fzs = fzsString.split(" ").map { it.trim().toDouble() }

        if (diameters.size != fzs.size) {
            warning("Input error", "Invalid input, please correct and try again")
        } else {
            runAsync {
                diameters.zip(fzs).forEach { pair ->
                    val newFz = Fz().apply {
                        millingProfileEntry = entry.id
                        diameter = pair.first
                        fz = pair.second
                    }
                    db.saveFz(newFz, false)
                }
            } ui {
                config["diametersDefault"] = diametersString
                config.save()
                fire(FzAdded())
            }
        }
    }

    override fun onCreate() {
        val entry = MillingProfileEntryModel(MillingProfileEntry().apply {
            millingProfile = profile.item
        })
        val diameters = SimpleStringProperty(config.string("diametersDefault", ""))
        val fzs = SimpleStringProperty()

        tornadofx.runAsync {
            db.listMaterials() to db.listOperations()
        } ui { (materials, operations) ->
            // Default to previously used material
            config.int("materialDefault")?.also { default ->
                entry.material.value = materials.find { it.id == default }
            }

            dialog("Add data for given material and cutting operation") {
                form {
                    fieldset {
                        field("Material") {
                            combobox(entry.material, materials).required()
                        }
                        field("Operation") {
                            combobox(entry.operation, operations).required()
                        }
                        field("aeMax") {
                            textfield(entry.aeMax)
                        }
                        field("apMax") {
                            textfield(entry.apMax)
                        }
                        field("Vc") {
                            textfield(entry.vc).required()
                        }
                        label("Optional: Space separated list of diameters followed by a space separated list of corresponding fz values")
                        fieldset {
                            field("Diameters") {
                                textfield(diameters)
                            }
                            field("Fz values") {
                                textfield(fzs)
                            }
                        }
                    }
                    button("Create") {
                        isDefaultButton = true

                        action {
                            entry.commit {
                                tornadofx.runAsync {
                                    db.insertMillingProfileEntry(entry.item)
                                } ui {
                                    addMultiFz(entry.item, diameters.value, fzs.value)
                                    config["materialDefault"] = entry.material.value.id.toString()
                                    config.save()
                                    close()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}