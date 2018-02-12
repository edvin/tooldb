package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*
import java.sql.ResultSet

class Fz() {
    val millingProfileEntryProperty = SimpleIntegerProperty()
    var millingProfileEntry by millingProfileEntryProperty

    val diameterProperty = SimpleDoubleProperty()
    var diameter by diameterProperty

    val fzProperty = SimpleDoubleProperty(this, "fz")
    var fz by fzProperty

    constructor(rs: ResultSet) : this() {
        with(rs) {
            millingProfileEntry = rs.getInt("milling_profile_entry")
            diameter = rs.getDouble("diameter")
            fz = rs.getDouble("fz")
        }
    }
}

class FzModel(fz: Fz? = null) : ItemViewModel<Fz>(fz) {
    val millingProfileEntry = bind(Fz::millingProfileEntryProperty)
    val diameter = bind(Fz::diameterProperty)
    val fz = bind(Fz::fzProperty)
}

class FzAdded(val fz: Fz? = null) : FXEvent()