package com.turbointernational.metadatarefactored.metadatarefactored.migration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.sql.ResultSet
import javax.sql.DataSource
import com.turbointernational.metadatarefactored.metadatarefactored.dao.*
import com.turbointernational.metadatarefactored.metadatarefactored.model.Attribute
import com.turbointernational.metadatarefactored.metadatarefactored.model.Part
import com.turbointernational.metadatarefactored.metadatarefactored.model.PartType


/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Service
class MigrationService(@Qualifier("accessDataSource") private val accessDataSource: DataSource,
                       @Qualifier("mysqlDataSource") private val mysqlDataSource: DataSource,
                       private val objectMapper: ObjectMapper,
                       private val partTypeDao: PartTypeDao,
                       private val attributeDao: AttributeDao,
                       private val partDao: PartDao) {

    private val ENGINE = "engine"
    private val VEHICLE = "vehicle"

    private val engineAccessSelect = "SELECT ec.EngineConfigID, ed.EngineDesignationName, ec.EngineVINID, ec.ValvesID,\n" +
            "       eb.Liter, eb.CC, eb.CID, eb.Cylinders, eb.BlockType, eb.EngBoreIn, \n" +
            "       eb.EngBoreMetric, eb.EngStrokeIn, eb.EngStrokeMetric,\n" +
            "       ec.FuelDeliveryConfigID, a.AspirationName,\n" +
            "       ec.CylinderHeadTypeID,\n" +
            "       ft.FuelTypeName, ec.IgnitionSystemTypeID,\n" +
            "       mfr.MfrName, ev.EngineVersion, po.HorsePower, po.KilowattPower, \n" +
            "       (SELECT array_agg(VehicleId) FROM VehicleToEngineConfig WHERE EngineConfigID = ec.EngineConfigID) AS vehicleIds \n" +
            "FROM EngineConfig ec\n" +
            " INNER JOIN EngineDesignation ed ON ec.EngineDesignationID = ed.EngineDesignationID\n" +
            " INNER JOIN EngineBase eb ON ec.EngineBaseID = eb.EngineBaseID\n" +
            " INNER JOIN Aspiration a ON ec.AspirationID = a.AspirationID\n" +
            " INNER JOIN FuelType ft ON ec.FuelTypeID = ft.FuelTypeID\n" +
            " INNER JOIN Mfr mfr ON ec.EngineMfrID = mfr.MfrID\n" +
            " INNER JOIN EngineVersion ev ON ec.EngineVersionID = ev.EngineVersionID\n" +
            " INNER JOIN PowerOutput po ON ec.PowerOutputId = po.PowerOutputId"

    private val engineMysqlSelect = "SELECT engine_size as Liter, cft.name as FuelTypeName FROM car_engine ce\n" +
            " INNER JOIN car_fuel_type cft ON ce.car_fuel_type_id = cft.id"

    private val vehicleAccessSelect = "SELECT v.VehicleID, bv.YearId, m.MakeName, mo.ModelName, mo.VehicleTypeId\n" +
            "\t   sm.SubModelName, v.RegionID, v.Source, v.PublicationStageID, \n" +
            "\t   v.PublicationStageSource, v.PublicationStageDate, \n" +
            "       (SELECT array_agg(EngineConfigID) FROM VehicleToEngineConfig WHERE VehicleID = v.VehicleID) AS engineConfigIds\n" +
            "FROM Vehicle v\n" +
            " INNER JOIN BaseVehicle bv ON v.BaseVehicleID = bv.BaseVehicleID\n" +
            " INNER JOIN Make m ON bv.MakeID = m.MakeID\n" +
            " INNER JOIN Model mo ON bv.ModelID = mo.ModelID\n" +
            " INNER JOIN Submodel sm ON v.SubModelID = sm.SubModelID"

    private val vehicleMysqlSelect = "SELECT cmey.id as VehicleID, car_year_id as YearID, cma.name as MakeName, cm.name as ModelName FROM car_model_engine_year cmey\n" +
            " INNER JOIN car_model cm ON cmey.car_model_id = cm.id\n" +
            " INNER JOIN car_make cma ON cm.car_make_id = cma.id"

    private val queries = mapOf(ENGINE to Pair(engineAccessSelect, engineMysqlSelect), VEHICLE to Pair(vehicleAccessSelect, vehicleMysqlSelect))


    fun migrate() {
        createPartTypes()
        createAttributes()
        partTypeDao.findAll().forEach(::migrateCommon)
    }

    private fun createPartTypes() {
        partTypeDao.save(PartType(type = ENGINE))
        partTypeDao.save(PartType(type = VEHICLE))
    }

    private fun createAttributes() {
        partTypeDao.findAll().forEach {
            accessDataSource.connection.use {conn ->
                val resultSet = conn.prepareStatement(queries[it.type]!!.first).executeQuery()
                for(counter in 1 .. resultSet.metaData.columnCount) {
                    attributeDao.save(Attribute(name = resultSet.metaData.getColumnLabel(counter),
                            type = if (resultSet.metaData.getColumnLabel(counter) == "VEHICLEIDS" || resultSet.metaData.getColumnLabel(counter) == "ENGINECONFIGIDS") "A" else "S",
                            partType = it))
                }

            }
        }
    }


    private fun migrateCommon(partType: PartType) {
        val attributes = attributeDao.findByPartType(partType)
        queries[partType.type]?.let {
           accessDataSource.connection.use {
               val accessResultSet = it.prepareStatement(queries[partType.type]!!.first).executeQuery()
               while(accessResultSet.next()) partDao.save(convertToPart(accessResultSet, partType, attributes))
           }
           mysqlDataSource.connection.use {
               val mysqlResultSet = it.prepareStatement(queries[partType.type]!!.second).executeQuery()
               while(mysqlResultSet.next()) partDao.save(convertToPart(mysqlResultSet, partType, attributes))
           }
        }
    }

    private fun convertToPart(resultSet: ResultSet, partType: PartType, attributes: List<Attribute>) =
            Part(partType = partType, attributes = objectMapper.writeValueAsString(
                    attributes.filter { hasColumn(resultSet, it.name) }.map {
                        it.id to when (it.type) {"A" -> resultSet.getArray(it.name).array
                            else -> resultSet.getString(it.name)
                        }
                    }.toMap()))


    fun hasColumn(rs: ResultSet, columnName: String): Boolean {
        val metadata = rs.metaData
        val columns = metadata.columnCount
        for (x in 1..columns) {
            if (columnName == metadata.getColumnName(x) || columnName == metadata.getColumnLabel(x)) {
                return true
            }
        }
        return false
    }
}