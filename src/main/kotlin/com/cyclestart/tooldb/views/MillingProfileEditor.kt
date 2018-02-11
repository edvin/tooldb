package com.cyclestart.tooldb.views

import com.cyclestart.tooldb.models.MillingProfileModel
import tornadofx.*

class MillingProfileEditor : View("Edit milling profile") {
    val profile : MillingProfileModel by inject()

    override val root = form {
        fieldset {
            field("Name") {
                textfield(profile.name)
            }
        }
    }
}