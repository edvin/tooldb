package com.cyclestart.tooldb.controllers

import com.cyclestart.tooldb.models.*
import com.zaxxer.hikari.HikariDataSource
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

class DB : Controller() {
    val ds = HikariDataSource().apply {
        jdbcUrl = "jdbc:mysql://mysql193359.mysql.sysedata.no/mysql193359"
        username = "mysql193359"
        password = System.getenv("dbPassword")
    }

    init {
        beforeShutdown {
            ds.close()
        }
    }

    fun listVendors() = ds.withConnection {
        prepareStatement("""
            SELECT id, name
            FROM vendor
            ORDER BY name""").toModel { Vendor(it) }
    }

    fun insertVendor(vendor: Vendor) = ds.withConnection {
        prepareStatement("INSERT INTO vendor (name) VALUES (?)").exec {
            setString(1, vendor.name)
        }
    }

    fun updateVendor(vendor: Vendor) = ds.withConnection {
        prepareStatement("UPDATE vendor SET name = ? WHERE id = ?").exec {
            setString(1, vendor.name)
            setInt(2, vendor.id)
        }
    }

    fun deleteVendor(vendor: Vendor) = ds.withConnection {
        prepareStatement("DELETE FROM vendor WHERE id = ?").exec {
            setInt(1, vendor.id)
        }
    }

    fun listOperations() = ds.withConnection {
        prepareStatement("""
            SELECT id, name
            FROM operation
            ORDER BY name""").toModel { Operation(it) }
    }

    fun insertOperation(operation: Operation) = ds.withConnection {
        prepareStatement("INSERT INTO operation (name) VALUES (?)").exec {
            setString(1, operation.name)
        }
    }

    fun updateOperation(operation: Operation) = ds.withConnection {
        prepareStatement("UPDATE operation SET name = ? WHERE id = ?").exec {
            setString(1, operation.name)
            setInt(2, operation.id)
        }
    }

    fun deleteOperation(Operation: Operation) = ds.withConnection {
        prepareStatement("DELETE FROM operation WHERE id = ?").exec {
            setInt(1, Operation.id)
        }
    }

    fun listMaterials() = ds.withConnection {
        prepareStatement("""
            SELECT id, name, hardness
            FROM material
            ORDER BY name""").toModel { Material(it) }
    }

    fun insertMaterial(material: Material) = ds.withConnection {
        prepareStatement("INSERT INTO material (name, hardness) VALUES (?, ?)").exec {
            setString(1, material.name)
            setString(2, material.hardness)
        }
    }

    fun updateMaterial(material: Material) = ds.withConnection {
        prepareStatement("UPDATE material SET name = ?, hardness = ? WHERE id = ?").exec {
            setString(1, material.name)
            setString(2, material.hardness)
            setInt(3, material.id)
        }
    }

    fun deleteMaterial(material: Material) = ds.withConnection {
        prepareStatement("DELETE FROM material WHERE id = ?").exec {
            setInt(1, material.id)
        }
    }

    fun listToolTypes() = ds.withConnection {
        prepareStatement("""
            SELECT id, name
            FROM tool_type
            ORDER BY name""").toModel { Vendor(it) }
    }

    fun insertToolType(toolType: ToolType) = ds.withConnection {
        prepareStatement("INSERT INTO tool_type (name) VALUES (?)").exec {
            setString(1, toolType.name)
        }
    }

    fun updateToolType(toolType: ToolType) = ds.withConnection {
        prepareStatement("UPDATE tool_type SET name = ? WHERE id = ?").exec {
            setString(1, toolType.name)
            setInt(2, toolType.id)
        }
    }

    fun deleteToolType(toolType: ToolType) = ds.withConnection {
        prepareStatement("DELETE FROM tool_type WHERE id = ?").exec {
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
            ORDER BY t.description""").toModel { Vendor(it) }
    }

    fun insertTool(tool: Tool) = ds.withConnection {
        prepareStatement("""
            INSERT INTO tool (tool_type, product_id, vendor, product_link, description) VALUES (?, ?, ?, ?, ?)"""
        ).execWithKeys({
            setInt(1, tool.toolType.id)
            setString(2, tool.productId)
            setInt(3, tool.vendor.id)
            setString(4, tool.productLink)
            setString(5, tool.description)
        }, {
            tool.id = getInt(1)
        })
    }

    fun updateTool(tool: Tool) = ds.withConnection {
        prepareStatement("""
            UPDATE tool
            SET description = ?, tool_type = ?, vendor = ?, product_link = ?
            WHERE id = ?""").exec {
            setString(1, tool.description)
            setInt(2, tool.toolType.id)
            setInt(3, tool.vendor.id)
            setString(4, tool.productLink)
            setInt(5, tool.id)
        }
    }

    fun deleteTool(tool: Tool) = ds.withConnection {
        prepareStatement("DELETE FROM tool WHERE id = ?").exec {
            setInt(1, tool.id)
        }
    }

    fun listMillingProfiles() = ds.withConnection {
        prepareStatement("""
            SELECT p.id, p.name, p.material, m.name AS material_name, p.operation, o.name AS operation_name, p.ae_max, p.ap_max, p.vc
            FROM milling_profile p, material m, operation o
            WHERE p.material = m.id AND p.operation = o.id
            ORDER BY p.name""").toModel { MillingProfile(it) }
    }

    fun insertMillingProfile(profile: MillingProfile) = ds.withConnection {
        prepareStatement("INSERT INTO milling_profile (name, material, operation, ae_max, ap_max, vc) VALUES (?, ?, ?, ?, ?, ?)").execWithKeys({
            setString(1, profile.name)
            setInt(2, profile.operation.id)
            setDouble(3, profile.aeMax)
            setDouble(4, profile.apMax)
            setInt(5, profile.vc)
        }, { profile.id = getInt(1) })
    }

    fun updateMillingProfile(profile: MillingProfile) = ds.withConnection {
        prepareStatement("UPDATE milling_profile SET name = ?, operation = ?, ae_max = ?, ap_max = ?, vc = ? WHERE id = ?").exec {
            setString(1, profile.name)
            setInt(2, profile.operation.id)
            setDouble(3, profile.aeMax)
            setDouble(4, profile.apMax)
            setInt(5, profile.vc)
            setInt(6, profile.id)
        }
    }

    fun deleteMillingProfile(profile: MillingProfile) = ds.withConnection {
        prepareStatement("DELETE FROM milling_profile WHERE id = ?").exec {
            setInt(1, profile.id)
        }
    }

}

/**
 * Apply the op function to the PreparedStatement and then call executeUpdate()
 * This allows you to write `prepareStatement(sql).exec { setParametersHere() }` to
 * set parameters easily and execute the sql effortlessly.
 *
 * This was not optimized further to `exec(sql) {}` because your IDE will
 * allow auto completion for the `prepareStatement` sql parameter but not for a custom function
 * as it can't recognize the sql anymore.
 */
fun PreparedStatement.exec(op: PreparedStatement.() -> Unit) = let {
    op(it)
    it.executeUpdate()
}

/**
 * Apply the op function to the PreparedStatement and then call executeUpdate()
 * This allows you to write `prepareStatement(sql).exec { setParametersHere() }` to
 * set parameters easily and execute the sql effortlessly.
 *
 * This was not optimized further to `exec(sql) {}` because your IDE will
 * allow auto completion for the `prepareStatement` sql parameter but not for a custom function
 * as it can't recognize the sql anymore.
 */
fun PreparedStatement.execWithKeys(op: PreparedStatement.() -> Unit, keys: ResultSet.() -> Unit) = let {
    op(it)
    it.executeUpdate()
    keys(generatedKeys)
}

/**
 * Fetch a connection from the datasource and apply the op function to it before
 * closing the connection.
 */
fun <T> DataSource.withConnection(op: Connection.() -> T) = connection.use { op(it) }

/**
 * Call executeQuery() on the PreparedStatement and call the fn parameter on each
 * row in the ResultSet to create an observable list of items extracted from the rows.
 */
fun <T> PreparedStatement.toModel(fn: (ResultSet) -> T) = executeQuery().toModel(fn)

/**
 * Call the fn parameter on each row in the ResultSet to create an observable list of items extracted from the rows.
 */
fun <T> ResultSet.toModel(fn: (ResultSet) -> T): ObservableList<T> {
    val list = FXCollections.observableArrayList<T>()
    while (next()) list.add(fn(this))
    return list
}