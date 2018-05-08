package com.turbointernational.metadatarefactored.metadatarefactored.model

import com.arangodb.springframework.annotation.Document
import javax.persistence.*
import javax.persistence.Entity

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@Entity(name = "parts")
@Document
data class Part(@Id @GeneratedValue var id: Long? = null,
                @ManyToOne @JoinColumn(name="part_type_id") var partType: PartType,
                @Column(length = 20000) var attributes: String)