package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.controllers.DB
import com.cyclestart.tooldb.models.MillingProfile
import com.cyclestart.tooldb.models.MillingProfileAdded
import com.cyclestart.tooldb.models.MillingProfileModel
import tornadofx.*

class MillingProfileList : View("MillingProfiles") {
    val db: DB by inject()
    val selectedMillingProfile = MillingProfileModel()
    override val deletable = !selectedMillingProfile.empty

    override val root = tableview<MillingProfile> {
        column("Name", MillingProfile::nameProperty)
        smartResize()
        subscribe<MillingProfileAdded> { onRefresh() }
        bindSelected(selectedMillingProfile)
        onUserSelect { editProfile(selectedMillingProfile.item) }
    }

    private fun editProfile(profile: MillingProfile) {
        workspace.dockInNewScope<MillingProfileEditor>(MillingProfileModel(profile))
    }

    override fun onDelete() {
        confirm("Confirm milling profile delete", "Do you want to delete ${selectedMillingProfile.name.value}?") {
            runAsync {
                db.deleteMillingProfile(selectedMillingProfile.item)
            } ui {
                onRefresh()
            }
        }
    }

    override fun onDock() {
        onRefresh()
    }

    override fun onRefresh() {
        root.asyncItems { db.listMillingProfiles() }
    }

    override fun onCreate() {
        val profile = MillingProfileModel(MillingProfile())

        dialog("Add Milling Profile") {
            form {
                fieldset {
                    field("Name") {
                        textfield(profile.name).required()
                    }
                }
                button("Create") {
                    isDefaultButton = true

                    action {
                        profile.commit {
                            runAsync {
                                db.insertMillingProfile(profile.item)
                            } ui {
                                close()
                                editProfile(profile.item)
                            }
                        }
                    }
                }
            }
        }
    }

}