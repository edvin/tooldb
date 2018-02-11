package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.sql.ResultSet

class MillingProfile() {
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

class MillingProfileModel(profile: MillingProfile? = null) : ItemViewModel<MillingProfile>(profile) {
    val id = bind(MillingProfile::idProperty)
    val name = bind(MillingProfile::nameProperty)
}


class MillingProfileAdded(val profile: MillingProfile) : FXEvent()
