package com.turbointernational.metadatarefactored.metadatarefactored.model

import com.arangodb.springframework.annotation.Document
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Entity(name = "part_types")
@Document
data class PartType(@Id @GeneratedValue var id: Long? = null,
                    var type: String)