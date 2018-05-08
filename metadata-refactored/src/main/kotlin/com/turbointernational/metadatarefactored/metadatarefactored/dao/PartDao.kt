package com.turbointernational.metadatarefactored.metadatarefactored.dao

import com.turbointernational.metadatarefactored.metadatarefactored.model.Part

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
interface PartDao: CommonDao<Part> {
    fun findByPartTypeId(partTypeId: Long): List<Part>
}