package com.turbointernational.metadatarefactored.metadatarefactored.dao

import com.turbointernational.metadatarefactored.metadatarefactored.model.Attribute
import com.turbointernational.metadatarefactored.metadatarefactored.model.PartType

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
interface AttributeDao: CommonDao<Attribute> {
    fun findByPartType(partType: PartType): List<Attribute>
}