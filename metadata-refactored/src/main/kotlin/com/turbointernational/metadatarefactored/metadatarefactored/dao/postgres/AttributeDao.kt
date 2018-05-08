package com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.turbointernational.metadatarefactored.metadatarefactored.model.*

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Repository
interface AttributeDao: JpaRepository<Attribute, Long> {
    fun findByEntityType(entityType: EntityType): List<Attribute>
}