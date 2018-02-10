package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.controllers.DB
import com.cyclestart.tooldb.models.Vendor
import com.cyclestart.tooldb.models.VendorAdded
import com.cyclestart.tooldb.models.VendorModel
import tornadofx.*

class VendorList : View("Vendors") {
    val db: DB by inject()
    val selectedVendor = VendorModel()
    override val deletable = !selectedVendor.empty

    override val root = tableview<Vendor> {
        column("Name", Vendor::nameProperty).makeEditable()
        smartResize()
        onEditCommit {
            runAsync { db.updateVendor(it) }
        }
        subscribe<VendorAdded> { onRefresh() }
        bindSelected(selectedVendor)
    }

    override fun onDelete() {
        confirm("Confirm vendor delete", "Do you want to delete ${selectedVendor.name.value}?") {
            runAsync {
                db.deleteVendor(selectedVendor.item)
            } ui {
                onRefresh()
            }
        }
    }

    override fun onDock() {
        onRefresh()
    }

    override fun onRefresh() {
        root.asyncItems { db.listVendors() }
    }

    override fun onCreate() {
        val vendor = VendorModel(Vendor())

        dialog("Add Vendor") {
            form {
                fieldset {
                    field("Name") {
                        textfield(vendor.name).required()
                    }
                }
                button("Create").action {
                    vendor.commit {
                        runAsync {
                            db.insertVendor(vendor.item)
                        } ui {
                            close()
                        }
                    }
                }
            }
        }
    }

}