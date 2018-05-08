package com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres

import com.turbointernational.metadatarefactored.metadatarefactored.model.Entity
import com.turbointernational.metadatarefactored.metadatarefactored.model.EntityType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Repository
interface EntityTypeDao: JpaRepository<EntityType, Long> {
    fun findByType(type: String): EntityType
}