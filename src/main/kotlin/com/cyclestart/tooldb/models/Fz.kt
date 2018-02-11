package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.getValue
import tornadofx.setValue
import java.sql.ResultSet

class Fz() {
    val millingProfileEntryProperty = SimpleIntegerProperty()
    var millingProfileEntry by millingProfileEntryProperty

    val diameterProperty = SimpleDoubleProperty()
    var diameter by diameterProperty

    val fzProperty = SimpleDoubleProperty()
    var fz by fzProperty

    constructor(rs: ResultSet) : this() {
        with(rs) {
            millingProfileEntry = rs.getInt("milling_profile_entry")
            diameter = rs.getDouble("diameter")
            fz = rs.getDouble("fz")
        }
    }
}
