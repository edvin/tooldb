package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.sql.ResultSet

class ToolType() {
    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    constructor(rs: ResultSet) : this() {
        with(rs) {
            id = getInt("id")
            name = getString("name")
        }
    }
}

class ToolTypeModel(toolType: ToolType? = null) : ItemViewModel<ToolType>(toolType) {
    val id = bind(ToolType::idProperty)
    val name = bind(ToolType::nameProperty)
}

class ToolTypeAdded(val toolType: ToolType) : FXEvent()
