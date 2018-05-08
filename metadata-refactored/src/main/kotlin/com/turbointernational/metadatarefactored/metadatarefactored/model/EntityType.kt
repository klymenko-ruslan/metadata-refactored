package com.turbointernational.metadatarefactored.metadatarefactored.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Entity(name = "entity_types")
data class EntityType(@Id @GeneratedValue var id: Long? = null,
                      var type: String)