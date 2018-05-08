package com.turbointernational.metadatarefactored.metadatarefactored.migration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.sql.ResultSet
import javax.sql.DataSource
import com.turbointernational.metadatarefactored.metadatarefactored.model.*
import com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres.*

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Service
class MigrationService(@Qualifier("accessDataSource") private val accessDataSource: DataSource,
                       @Qualifier("mysqlDataSource") private val mysqlDataSource: DataSource,
                       private val objectMapper: ObjectMapper,
                       private val entityTypeDao: EntityTypeDao,
                       private val attributeDao: AttributeDao,
                       private val entityDao: EntityDao) {

    private val engineAccessSelect = "SELECT ec.EngineConfigID, ed.EngineDesignationName, ec.EngineVINID, ec.ValvesID,\n" +
            "       eb.Liter, eb.CC, eb.CID, eb.Cylinders, eb.BlockType, eb.EngBoreIn, \n" +
            "       eb.EngBoreMetric, eb.EngStrokeIn, eb.EngStrokeMetric,\n" +
            "       ec.FuelDeliveryConfigID, a.AspirationName,\n" +
            "       ec.CylinderHeadTypeID,\n" +
            "       ft.FuelTypeName, ec.IgnitionSystemTypeID,\n" +
            "       mfr.MfrName, ev.EngineVersion, po.HorsePower, po.KilowattPower\n" +
            "FROM EngineConfig ec\n" +
            " INNER JOIN EngineDesignation ed ON ec.EngineDesignationID = ed.EngineDesignationID\n" +
            " INNER JOIN EngineBase eb ON ec.EngineBaseID = eb.EngineBaseID\n" +
            " INNER JOIN Aspiration a ON ec.AspirationID = a.AspirationID\n" +
            " INNER JOIN FuelType ft ON ec.FuelTypeID = ft.FuelTypeID\n" +
            " INNER JOIN Mfr mfr ON ec.EngineMfrID = mfr.MfrID\n" +
            " INNER JOIN EngineVersion ev ON ec.EngineVersionID = ev.EngineVersionID\n" +
            " INNER JOIN PowerOutput po ON ec.PowerOutputId = po.PowerOutputId;"

    private val engineMysqlSelect = "SELECT engine_size as Liter, cft.name as FuelTypeName FROM car_engine ce\n" +
            " INNER JOIN car_fuel_type cft ON ce.car_fuel_type_id = cft.id;"

    fun migrate() {
        migrateEngine()
    }
    private fun migrateEngine() {

        val entityType = entityTypeDao.findByType("engine")
        val attributes = attributeDao.findByEntityType(entityType)


        val accessStatement = accessDataSource.connection.prepareStatement(engineAccessSelect)
        val accessResultSet = accessStatement.executeQuery()
        val entities = mutableListOf<Entity>()
        while(accessResultSet.next()) {
            //entityDao.save()
            entities.add(engineToEntity(accessResultSet, entityType, attributes))
        }

        val mysqlStatement = mysqlDataSource.connection.prepareStatement(engineMysqlSelect)
        val mysqlResultSet = mysqlStatement.executeQuery()
        while(accessResultSet.next()) {
            //entityDao.save()
            entities.add(engineToEntity(mysqlResultSet, entityType, attributes))
        }
        println(entities)
    }
    private fun engineToEntity(resultSet: ResultSet, entityType: EntityType, attributes: List<Attribute>) =
            Entity(entityType = entityType, attributes = objectMapper.writeValueAsString(
                    attributes.map { it.name to resultSet.getString(it.name) }.toMap()))
}