package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.sql.ResultSet

class Material() {
    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    val hardnessProperty = SimpleStringProperty()
    var hardness by hardnessProperty

    constructor(rs: ResultSet) : this() {
        with(rs) {
            id = getInt("id")
            name = getString("name")
            hardness = getString("hardness")
        }
    }
}