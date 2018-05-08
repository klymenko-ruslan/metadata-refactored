package com.turbointernational.metadatarefactored.metadatarefactored.controller

import com.turbointernational.metadatarefactored.metadatarefactored.dto.AttributeDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import com.turbointernational.metadatarefactored.metadatarefactored.service.AttributeService

/**
 * ruslan.klymenko@zorallabs.com 07.05.18
 */
@RestController
class AttributeController(private val attributeService: AttributeService) {

    @PostMapping("/attribute")
    fun postAttribute(@RequestBody attributeDto: AttributeDto) {
        attributeService.createAttribute(attributeDto)
    }

    @GetMapping("/attribute")
    fun getAttribute() = attributeService.getAttributes()

}
// entitytype in entity- use id


// entitytype/{id}/entity/