package com.turbointernational.metadatarefactored.metadatarefactored.service

import com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres.AttributeDao
import com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres.EntityTypeDao
import com.turbointernational.metadatarefactored.metadatarefactored.dto.AttributeDto
import com.turbointernational.metadatarefactored.metadatarefactored.model.Attribute
import org.springframework.stereotype.Service

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Service
class AttributeService(private val attributeDao: AttributeDao,
                       private val entityTypeDao: EntityTypeDao) {

    fun createAttribute(attributeDto: AttributeDto) = attributeDao.save(toAttribute(attributeDto))

    fun getAttributes() = attributeDao.findAll().map { toAttributeDto(it) }

    private fun toAttribute(attributeDto: AttributeDto) =
            Attribute(name = attributeDto.name ?: "", type = attributeDto.type ?: "", entityType = entityTypeDao.getOne(attributeDto.entityTypeId!!))
    private fun toAttributeDto(attribute: Attribute) = AttributeDto(attributeSetName = attribute?.attributeSet?.name ?: "", name = attribute.name, type = attribute.type, entityTypeId = attribute.entityType.id!!)
}