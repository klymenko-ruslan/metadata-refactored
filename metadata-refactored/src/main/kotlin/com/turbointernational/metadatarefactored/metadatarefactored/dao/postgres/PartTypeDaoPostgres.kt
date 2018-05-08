package com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres

import com.turbointernational.metadatarefactored.metadatarefactored.dao.PartTypeDao
import com.turbointernational.metadatarefactored.metadatarefactored.model.PartType
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Repository
@Profile("!arango")
interface PartTypeDaoPostgres: JpaRepository<PartType, Long>, PartTypeDao