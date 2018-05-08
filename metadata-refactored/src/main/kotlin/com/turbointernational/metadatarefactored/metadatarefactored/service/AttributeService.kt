package com.turbointernational.metadatarefactored.metadatarefactored.service

import com.turbointernational.metadatarefactored.metadatarefactored.dao.AttributeDao
import com.turbointernational.metadatarefactored.metadatarefactored.dao.PartTypeDao
import com.turbointernational.metadatarefactored.metadatarefactored.dto.AttributeDto
import com.turbointernational.metadatarefactored.metadatarefactored.model.Attribute
import org.springframework.stereotype.Service

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Service
class AttributeService(private val attributeDao: AttributeDao,
                       private val partTypeDao: PartTypeDao) {

    fun createAttribute(attributeDto: AttributeDto) = attributeDao.save(toAttribute(attributeDto))

    fun getAttributes() = attributeDao.findAll().map { toAttributeDto(it) }

    fun toAttribute(attributeDto: AttributeDto) =
            Attribute(name = attributeDto.name
                    ?: "", type = attributeDto.type
                    ?: "", partType = partTypeDao.findById(attributeDto.partTypeId!!).get())
    fun toAttributeDto(attribute: Attribute) = AttributeDto(attributeSetName = attribute?.attributeSet?.name ?: "", name = attribute.name, type = attribute.type, partTypeId = attribute.partType.id!!)


}