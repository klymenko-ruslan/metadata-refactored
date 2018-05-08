package com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres

import com.turbointernational.metadatarefactored.metadatarefactored.model.Entity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Repository
interface EntityDao: JpaRepository<Entity, Long>