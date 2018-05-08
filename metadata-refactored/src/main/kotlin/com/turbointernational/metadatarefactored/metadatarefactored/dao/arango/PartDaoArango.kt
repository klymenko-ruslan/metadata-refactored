package com.turbointernational.metadatarefactored.metadatarefactored.dao.arango

import com.arangodb.springframework.repository.ArangoRepository
import com.turbointernational.metadatarefactored.metadatarefactored.dao.PartDao
import com.turbointernational.metadatarefactored.metadatarefactored.model.Part
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Repository
@Profile("arango")
interface PartDaoArango: ArangoRepository<Part>, PartDao