package com.turbointernational.metadatarefactored.metadatarefactored.dto

import com.turbointernational.metadatarefactored.metadatarefactored.model.Attribute

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
data class AttributeDto(var attributeSetName: String? = null,
                        var name: String? = null,
                        var type: String? = null,
                        var partTypeId: Long? = null)
