package com.turbointernational.metadatarefactored.metadatarefactored.model

import com.arangodb.springframework.annotation.Document
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Entity(name = "attribute_sets")
@Document
data class AttributeSet(@Id @GeneratedValue var id: Long,
                        var name: String)