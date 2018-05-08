package com.turbointernational.metadatarefactored.metadatarefactored.service

import com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres.EntityTypeDao
import com.turbointernational.metadatarefactored.metadatarefactored.dto.EntityTypeDto
import org.springframework.stereotype.Service
import com.turbointernational.metadatarefactored.metadatarefactored.model.*

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Service
class EntityTypeService(private val entityTypeDao: EntityTypeDao) {

    fun create(entityTypeDto: EntityTypeDto) {
        entityTypeDao.save(toEntityType(entityTypeDto))
    }

    fun getEntityTypes() = entityTypeDao.findAll().map { toEntityTypeDto(it) }

    private fun toEntityType(entityTypeDto: EntityTypeDto) = EntityType(type = entityTypeDto.type ?: "")
    private fun toEntityTypeDto(entityType: EntityType) = EntityTypeDto(id = entityType.id, type = entityType.type)
}