package com.turbointernational.metadatarefactored.metadatarefactored.dao.postgres

import com.turbointernational.metadatarefactored.metadatarefactored.dao.PartDao
import com.turbointernational.metadatarefactored.metadatarefactored.model.Part
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Repository
@Profile("!arango")
interface PartDaoPostgres: JpaRepository<Part, Long>, PartDao {

}