package com.turbointernational.metadatarefactored.metadatarefactored.service

import com.turbointernational.metadatarefactored.metadatarefactored.dao.PartTypeDao
import com.turbointernational.metadatarefactored.metadatarefactored.dto.PartTypeDto
import org.springframework.stereotype.Service
import com.turbointernational.metadatarefactored.metadatarefactored.model.PartType

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Service
class PartTypeService(private val partTypeDao: PartTypeDao) {

    fun create(partTypeDto: PartTypeDto) {
        partTypeDao.save(toPartType(partTypeDto))
    }

    fun getPartTypes() = partTypeDao.findAll().map { toPartTypeDto(it) }

    private fun toPartType(partTypeDto: PartTypeDto) = PartType(type = partTypeDto.type
            ?: "")
    private fun toPartTypeDto(partType: PartType) = PartTypeDto(id = partType.id, type = partType.type)
}