package com.turbointernational.metadatarefactored.metadatarefactored.model

import javax.persistence.*
import javax.persistence.Entity

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Entity(name = "attributes")
data class Attribute(@Id @GeneratedValue var id: Long? = null,
                     @ManyToOne var attributeSet: AttributeSet? = null,
                     var name: String,
                     var type: String,
                     @ManyToOne @JoinColumn(name="entity_type_id") var entityType: EntityType)