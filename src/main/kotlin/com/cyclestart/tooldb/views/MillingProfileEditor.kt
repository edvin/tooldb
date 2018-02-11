package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.controllers.DB
import com.cyclestart.tooldb.models.*
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
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
                item("Add fz for new diameter") {
                    enableWhen(selectionModel.selectedItemProperty().isNotNull)
                    action {
                        selectedItem?.let { addFz(it) }
                    }
                }
            }
            smartResize()
        }
    }

    private fun addFz(entry: MillingProfileEntry) {
        val fz = FzModel(Fz().apply { millingProfileEntry = entry.id })

        dialog("Add fz for new diameter") {
            form {
                fieldset {
                    field("Diameter") {
                        textfield(fz.diameter).required()
                    }
                    field("Fz") {
                        textfield(fz.fz).required()
                    }
                }
                button("Add") {
                    isDefaultButton = true

                    action {
                        fz.commit {
                            runAsync {
                                db.insertFz(fz.item)
                            } ui {
                                close()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreate() {
        val entry = MillingProfileEntryModel(MillingProfileEntry().apply {
            millingProfile = profile.item
        })

        tornadofx.runAsync {
            db.listMaterials() to db.listOperations()
        } ui { (materials, operations) ->
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
                    }
                    button("Create") {
                        isDefaultButton = true

                        action {
                            entry.commit {
                                tornadofx.runAsync {
                                    db.insertMillingProfileEntry(entry.item)
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

}