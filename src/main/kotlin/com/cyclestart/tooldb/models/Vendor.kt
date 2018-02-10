package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.sql.ResultSet

class Vendor() {
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

class VendorModel(vendor: Vendor? = null) : ItemViewModel<Vendor>(vendor) {
    val id = bind(Vendor::idProperty)
    val name = bind(Vendor::nameProperty)
}

class VendorAdded(val vendor: Vendor) : FXEvent()
