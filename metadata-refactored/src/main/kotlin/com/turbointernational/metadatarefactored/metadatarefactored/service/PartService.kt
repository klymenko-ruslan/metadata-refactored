package com.turbointernational.metadatarefactored.metadatarefactored.service

import com.turbointernational.metadatarefactored.metadatarefactored.dao.PartDao
import com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres.PartDaoPostgres
import com.turbointernational.metadatarefactored.metadatarefactored.dto.PartDto
import com.turbointernational.metadatarefactored.metadatarefactored.model.Part
import org.springframework.stereotype.Service

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Service
class PartService(private val partDao: PartDao) {

    fun getParts() = partDao.findAll().map { toPartDto(it) }

    fun getPartsByTypeId(partTypeId: Long) = partDao.findByPartTypeId(partTypeId)

    private fun toPartDto(part: Part) = PartDto(id = part.id, partTypeId = part.partType.id, attributes = part.attributes)
}