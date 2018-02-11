package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import tornadofx.*
import java.sql.ResultSet

class Material() {
    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    val hardnessProperty = SimpleStringProperty()
    var hardness by hardnessProperty

    val colorProperty = SimpleObjectProperty<Color>()
    var color by colorProperty

    constructor(rs: ResultSet) : this() {
        with(rs) {
            id = getInt("id")
            name = getString("name")
            hardness = getString("hardness")
            color = c(getString("color"))
        }
    }

    override fun toString() = name
}

class MaterialModel(material: Material? = null) : ItemViewModel<Material>(material) {
    val id = bind(Material::idProperty)
    val name = bind(Material::nameProperty)
    val hardness = bind(Material::hardnessProperty)
}

class MaterialAdded(val material: Material) : FXEvent()