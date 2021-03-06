package com.cyclestart.tooldb.controllers

import com.cyclestart.tooldb.models.*
import com.zaxxer.hikari.HikariDataSource
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import tornadofx.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement.RETURN_GENERATED_KEYS
import javax.sql.DataSource

class DB : Controller() {
    val ds = HikariDataSource().apply {
        jdbcUrl = "jdbc:mysql://mysql193359.mysql.sysedata.no/mysql193359"
        username = "mysql193359"
        password = System.getenv("dbPassword")
    }

    // Used to track borrowed connections so we can show DB progress indicator in the Workspace
    internal val borrowedConnections = FXCollections.observableArrayList<Connection>()

    init {
        beforeShutdown {
            ds.close()
        }
    }

    fun listVendors() = ds.withConnection {
        prepareStatement("""
            SELECT id, name
            FROM vendor
            ORDER BY name""").query().toModel { Vendor(it) }
    }

    fun insertVendor(vendor: Vendor) = ds.withConnection {
        prepareStatement("INSERT INTO vendor (name) VALUES (?)").update {
            setString(1, vendor.name)
        }
        fire(VendorAdded(vendor))
    }

    fun updateVendor(vendor: Vendor) = ds.withConnection {
        prepareStatement("UPDATE vendor SET name = ? WHERE id = ?").update {
            setString(1, vendor.name)
            setInt(2, vendor.id)
        }
    }

    fun deleteVendor(vendor: Vendor) = ds.withConnection {
        prepareStatement("DELETE FROM vendor WHERE id = ?").update {
            setInt(1, vendor.id)
        }
    }

    fun listMillingProfiles() = ds.withConnection {
        prepareStatement("""
            SELECT id, name
            FROM milling_profile
            ORDER BY name""").query().toModel { MillingProfile(it) }
    }

    fun insertMillingProfile(profile: MillingProfile) = ds.withConnection {
        prepareStatement("INSERT INTO milling_profile (name) VALUES (?)").update {
            setString(1, profile.name)
        }
        fire(MillingProfileAdded(profile))
    }

    fun updateMillingProfile(profile: MillingProfile) = ds.withConnection {
        prepareStatement("UPDATE milling_profile SET name = ? WHERE id = ?").update {
            setString(1, profile.name)
            setInt(2, profile.id)
        }
    }

    fun deleteMillingProfile(milling_profile: MillingProfile) = ds.withConnection {
        prepareStatement("DELETE FROM milling_profile WHERE id = ?").update {
            setInt(1, milling_profile.id)
        }
    }

    fun listOperations() = ds.withConnection {
        prepareStatement("""
            SELECT id, name
            FROM operation
            ORDER BY name""").query().toModel { Operation(it) }
    }

    fun insertOperation(operation: Operation) = ds.withConnection {
        prepareStatement("INSERT INTO operation (name) VALUES (?)").update {
            setString(1, operation.name)
        }
    }

    fun updateOperation(operation: Operation) = ds.withConnection {
        prepareStatement("UPDATE operation SET name = ? WHERE id = ?").update {
            setString(1, operation.name)
            setInt(2, operation.id)
        }
    }

    fun deleteOperation(Operation: Operation) = ds.withConnection {
        prepareStatement("DELETE FROM operation WHERE id = ?").update {
            setInt(1, Operation.id)
        }
    }

    fun listMaterials() = ds.withConnection {
        prepareStatement("""
            SELECT id, name, hardness, color
            FROM material
            ORDER BY name""").query().toModel { Material(it) }
    }

    fun insertMaterial(material: Material) = ds.withConnection {
        prepareStatement("INSERT INTO material (name, hardness, color) VALUES (?, ?, ?)").update {
            setString(1, material.name)
            setString(2, material.hardness)
            setString(3, material.color.toString())
        }
        fire(MaterialAdded(material))
    }

    fun updateMaterial(material: Material) = ds.withConnection {
        prepareStatement("UPDATE material SET name = ?, hardness = ?, color = ? WHERE id = ?").update {
            setString(1, material.name)
            setString(2, material.hardness)
            setString(3, material.color.toString())
            setInt(4, material.id)
        }
    }

    fun deleteMaterial(material: Material) = ds.withConnection {
        prepareStatement("DELETE FROM material WHERE id = ?").update {
            setInt(1, material.id)
        }
    }

    fun listToolTypes() = ds.withConnection {
        prepareStatement("""
            SELECT id, name
            FROM tool_type
            ORDER BY name""").query().toModel { ToolType(it) }
    }

    fun insertToolType(toolType: ToolType) = ds.withConnection {
        prepareStatement("INSERT INTO tool_type (name) VALUES (?)").update {
            setString(1, toolType.name)
        }
        fire(ToolTypeAdded(toolType))
    }

    fun updateToolType(toolType: ToolType) = ds.withConnection {
        prepareStatement("UPDATE tool_type SET name = ? WHERE id = ?").update {
            setString(1, toolType.name)
            setInt(2, toolType.id)
        }
    }

    fun deleteToolType(toolType: ToolType) = ds.withConnection {
        prepareStatement("DELETE FROM tool_type WHERE id = ?").update {
            setInt(1, toolType.id)
        }
    }

    fun listTools() = ds.withConnection {
        prepareStatement("""
            SELECT t.id, t.tool_type, t.product_id, t.vendor, t.product_link, t.description,
            v.name AS vendor_name,
            tt.name AS tool_type_name
            FROM tool t, vendor v, tool_type tt
            WHERE t.tool_type = tt.id
            AND t.vendor = v.id
            ORDER BY t.description""").query().toModel { Tool(it) }
    }

    fun insertTool(tool: Tool) = ds.withConnection {
        prepareStatement("""
            INSERT INTO tool (tool_type, product_id, vendor, product_link, description) VALUES (?, ?, ?, ?, ?)""", RETURN_GENERATED_KEYS).execWithKeys({
            setInt(1, tool.toolType.id)
            setString(2, tool.productId)
            setInt(3, tool.vendor.id)
            setString(4, tool.productLink)
            setString(5, tool.description)
        }, {
            next()
            tool.id = getInt(1)
        })
    }

    fun updateTool(tool: Tool) = ds.withConnection {
        prepareStatement("""
            UPDATE tool
            SET description = ?, tool_type = ?, vendor = ?, product_link = ?
            WHERE id = ?""").update {
            setString(1, tool.description)
            setInt(2, tool.toolType.id)
            setInt(3, tool.vendor.id)
            setString(4, tool.productLink)
            setInt(5, tool.id)
        }
    }

    fun deleteTool(tool: Tool) = ds.withConnection {
        prepareStatement("DELETE FROM tool WHERE id = ?").update {
            setInt(1, tool.id)
        }
    }

    fun listEntriesForMillingProfile(profile: MillingProfile) = ds.withConnection {
        prepareStatement("""
            SELECT p.id, p.milling_profile, mp.name AS milling_profile_name, p.material, m.name AS material_name, m.hardness AS material_hardness, p.operation, o.name AS operation_name, p.ae_max, p.ap_max, p.vc
            FROM milling_profile_entry p, material m, operation o, milling_profile mp
            WHERE p.material = m.id AND p.operation = o.id AND p.milling_profile = mp.id
            AND p.milling_profile = ?
            ORDER BY m.name""").query {
            setInt(1, profile.id)
        }
                .toModel { MillingProfileEntry(it) }
                .also { connectFzToProfileEntries(it) }
    }

    fun insertMillingProfileEntry(entry: MillingProfileEntry) = ds.withConnection {
        prepareStatement("INSERT INTO milling_profile_entry (milling_profile, material, operation, ae_max, ap_max, vc) VALUES (?, ?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS).execWithKeys({
            setInt(1, entry.millingProfile.id)
            setInt(2, entry.material.id)
            setInt(3, entry.operation.id)
            setDouble(4, entry.aeMax)
            setDouble(5, entry.apMax)
            setInt(6, entry.vc)
        }, {
            next()
            entry.id = getInt(1)
        })
        fire(MillingProfileEntryAdded(entry))
    }

    private fun connectFzToProfileEntries(entries: List<MillingProfileEntry>) {
        val ids: List<Int> = entries.map { it.id }
        if (ids.isEmpty()) return
        val fzList = ds.withConnection {
            prepareStatement("SELECT * FROM fz WHERE milling_profile_entry IN (${ids.joinToString(",") { "?" }})").query {
                // MySQL doesn't support connection.createArray so we do it the awful way instead
                ids.forEachIndexed { index, i ->
                    setInt(index + 1, i)
                }
            }.toModel { Fz(it) }
        }

        entries.forEach { entry ->
            entry.fz = fzList.filter { it.millingProfileEntry == entry.id }.observable()
        }
    }

    fun insertFz(fz: Fz, fireAddEvent: Boolean = true) = ds.withConnection {
        prepareStatement("INSERT INTO fz (milling_profile_entry, diameter, fz) VALUES (?, ?, ?)").update {
            setInt(1, fz.millingProfileEntry)
            setDouble(2, fz.diameter)
            setDouble(3, fz.fz)
        }
        if (fireAddEvent) fire(FzAdded(fz))
    }

    fun updateFz(fz: Fz) = ds.withConnection {
        prepareStatement("UPDATE fz SET fz = ? WHERE milling_profile_entry = ? AND diameter = ?").update {
            setDouble(1, fz.fz)
            setInt(2, fz.millingProfileEntry)
            setDouble(3, fz.diameter)
        }
    }

    fun saveFz(fz: Fz, fireAddEvent: Boolean = true) {
        if (updateFz(fz) == 0) insertFz(fz, fireAddEvent)
    }

    fun deleteFz(fz: Fz) = ds.withConnection {
        prepareStatement("DELETE FROM fz WHERE milling_profile_entry = ? AND diameter = ?").update {
            setInt(1, fz.millingProfileEntry)
            setDouble(2, fz.diameter)
        }
    }

    fun updateMillingProfileEntry(entry: MillingProfileEntry) = ds.withConnection {
        prepareStatement("UPDATE milling_profile_entry SET operation = ?, ae_max = ?, ap_max = ?, vc = ? WHERE id = ?").update {
            setInt(1, entry.operation.id)
            setDouble(2, entry.aeMax)
            setDouble(3, entry.apMax)
            setInt(4, entry.vc)
            setInt(5, entry.id)
        }
    }

    fun deleteMillingProfileEntry(entry: MillingProfileEntry) = ds.withConnection {
        prepareStatement("DELETE FROM milling_profile WHERE id = ?").update {
            setInt(1, entry.id)
        }
    }

    /**
     * Apply the op function to the PreparedStatement and then call executeUpdate()
     * This allows you to write `prepareStatement(sql).update { setParametersHere() }` to
     * set parameters easily and execute the sql effortlessly.
     *
     * This was not optimized further to `update(sql) {}` because your IDE will
     * allow auto completion for the `prepareStatement` sql parameter but not for a custom function
     * as it can't recognize the sql anymore.
     */
    private fun PreparedStatement.update(op: PreparedStatement.() -> Unit): Int = let {
        op(it)
        it.executeUpdate()
    }

    /**
     * Apply the op function to the PreparedStatement and then call executeQuery()
     * This allows you to write `prepareStatement(sql).query { setParametersHere() }` to
     * set parameters easily and execute the sql effortlessly and return the ResultSet.
     *
     * This was not optimized further to `query(sql) {}` because your IDE will
     * allow auto completion for the `prepareStatement` sql parameter but not for a custom function
     * as it can't recognize the sql anymore.
     */
    private fun PreparedStatement.query(op: (PreparedStatement.() -> Unit)? = null): ResultSet = let {
        op?.invoke(it)
        it.executeQuery()
    }

    /**
     * Apply the op function to the PreparedStatement and then call executeUpdate()
     * This allows you to write `prepareStatement(sql).update { setParametersHere() }` to
     * set parameters easily and execute the sql effortlessly.
     *
     * This was not optimized further to `exec(sql) {}` because your IDE will
     * allow auto completion for the `prepareStatement` sql parameter but not for a custom function
     * as it can't recognize the sql anymore.
     */
    private fun PreparedStatement.execWithKeys(op: PreparedStatement.() -> Unit, keys: ResultSet.() -> Unit) = let {
        op(it)
        it.executeUpdate()
        keys(generatedKeys)
    }

    /**
     * Fetch a connection from the datasource and apply the op function to it before
     * closing the connection.
     */
    private fun <T> DataSource.withConnection(op: Connection.() -> T) = connection.use {
        try {
            borrowedConnections.add(it)
            op(it)
        } finally {
            borrowedConnections.remove(it)
        }
    }

    /**
     * Call the fn parameter on each row in the ResultSet to create an observable list of items extracted from the rows.
     */
    private fun <T> ResultSet.toModel(fn: (ResultSet) -> T): ObservableList<T> {
        val list = FXCollections.observableArrayList<T>()
        while (next()) list.add(fn(this))
        return list
    }

}

class DBActiveIndicator : Fragment() {
    val db: DB by inject()

    override val root = progressindicator {
        isVisible = false
        setPrefSize(16.0, 16.0)
    }

    init {
        db.borrowedConnections.addListener(ListChangeListener {
            root.isVisible = db.borrowedConnections.isNotEmpty()
        })
    }
}
