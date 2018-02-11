package com.cyclestart.tooldb.models

import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.scene.control.TreeItem
import tornadofx.*
import java.sql.ResultSet

class MillingProfileEntry() {
    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    val millingProfileProperty = SimpleObjectProperty<MillingProfile>()
    var millingProfile by millingProfileProperty

    val materialProperty = SimpleObjectProperty<Material>()
    var material by materialProperty

    val operationProperty = SimpleObjectProperty<Operation>()
    var operation by operationProperty

    val aeMaxProperty = SimpleDoubleProperty()
    var aeMax by aeMaxProperty

    val apMaxProperty = SimpleDoubleProperty()
    var apMax by apMaxProperty

    val vcProperty = SimpleIntegerProperty()
    var vc by vcProperty

    val fzProperty = SimpleListProperty<Fz>(FXCollections.observableArrayList())
    var fz by fzProperty

    constructor(rs: ResultSet) : this() {
        with(rs) {
            id = getInt("id")
            material = Material().apply {
                id = getInt("material")
                name = getString("material_name")
                hardness = getString("material_hardness")
            }
            operation = Operation().apply {
                id = getInt("operation")
                name = getString("operation_name")
            }
            millingProfile = MillingProfile().apply {
                id = getInt("milling_profile")
                name = getString("milling_profile_name")
            }
            aeMax = getDouble("ae_max")
            apMax = getDouble("ap_max")
            vc = getInt("vc")
        }
    }
}

class MillingProfileEntryModel(entry: MillingProfileEntry? = null) : ItemViewModel<MillingProfileEntry>(entry) {
    val id = bind(MillingProfileEntry::idProperty)
    val material = bind(MillingProfileEntry::materialProperty)
    val operation = bind(MillingProfileEntry::operationProperty)
    val aeMax = bind(MillingProfileEntry::aeMaxProperty)
    val apMax = bind(MillingProfileEntry::apMaxProperty)
    val vc = bind(MillingProfileEntry::vcProperty)
    val fz = bind(MillingProfileEntry::fzProperty)
}

class MillingProfileEntryAdded(val entry: MillingProfileEntry) : FXEvent()