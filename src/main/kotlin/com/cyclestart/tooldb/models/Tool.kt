package com.cyclestart.tooldb.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.sql.ResultSet

class Tool() {
    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    val toolTypeProperty = SimpleObjectProperty<ToolType>()
    var toolType by toolTypeProperty

    val descriptionProperty = SimpleStringProperty()
    var description by descriptionProperty

    val productIdProperty = SimpleStringProperty()
    var productId by productIdProperty

    val vendorProperty = SimpleObjectProperty<Vendor>()
    var vendor by vendorProperty

    val productLinkProperty = SimpleStringProperty()
    var productLink by productLinkProperty

    constructor(rs: ResultSet) : this() {
        with(rs) {
            id = getInt("id")
            description = getString("description")
            toolType = ToolType().apply {
                id = getInt("tool_type")
                name = getString("tool_type_name")
            }
            productId = getString("product_id")
            vendor = Vendor().apply {
                id = getInt("vendor")
                name = getString("vendor_name")
            }
            productLink = getString("product_link")
        }
    }
}