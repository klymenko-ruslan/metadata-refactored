package com.turbointernational.metadatarefactored.metadatarefactored.dao.arango

import com.arangodb.springframework.repository.ArangoRepository
import com.turbointernational.metadatarefactored.metadatarefactored.dao.PartTypeDao
import com.turbointernational.metadatarefactored.metadatarefactored.model.PartType
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@Repository
@Profile("arango")
interface PartTypeDaoArango: ArangoRepository<PartType>, PartTypeDao