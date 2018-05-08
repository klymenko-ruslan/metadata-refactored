package com.turbointernational.metadatarefactored.metadatarefactored.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Entity(name = "entities")
data class Entity(@Id @GeneratedValue var id: Long? = null,
                  @ManyToOne @JoinColumn(name="entity_type_id") var entityType: EntityType,
                  var attributes: String)