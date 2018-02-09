package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.sql.ResultSet

class MillingProfile() {
    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    val nameProperty = SimpleStringProperty()
    var name by nameProperty

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

    constructor(rs: ResultSet) : this() {
        with(rs) {
            id = getInt("id")
            name = getString("name")
            material = Material().apply {
                id = getInt("material")
                name = getString("material_name")
            }
            operation = Operation().apply {
                id = getInt("operation")
                name = getString("operation_name")
            }
            aeMax = getDouble("ae_max")
            apMax = getDouble("ap_max")
            vc = getInt("vc")
        }
    }
}