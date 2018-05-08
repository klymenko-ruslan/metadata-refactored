package com.turbointernational.metadatarefactored.metadatarefactored.model

import com.arangodb.springframework.annotation.Document
import javax.persistence.*
import javax.persistence.Entity

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Entity(name = "attributes")
@Document
data class Attribute(@Id @GeneratedValue var id: Long? = null,
                     @ManyToOne var attributeSet: AttributeSet? = null,
                     var name: String,
                     var type: String,
                     @ManyToOne @JoinColumn(name="part_type_id") var partType: PartType)
