package com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres

import com.turbointernational.metadatarefactored.metadatarefactored.dao.AttributeDao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.turbointernational.metadatarefactored.metadatarefactored.model.Attribute
import org.springframework.context.annotation.Profile

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Repository
@Profile("!arango")
interface AttributeDaoPostgres: JpaRepository<Attribute, Long>, AttributeDao