package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.controllers.DB
import com.cyclestart.tooldb.models.Vendor
import tornadofx.*

class VendorList : View("Vendors") {
    val db: DB by inject()

    override val root = borderpane {
        center {
            tableview<Vendor> {
                column("Name", Vendor::nameProperty).makeEditable()
                smartResize()
                asyncItems { db.listVendors() }
                onEditCommit {
                    runAsync { db.updateVendor(it) }
                }
            }
        }
    }
}