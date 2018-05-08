package com.turbointernational.metadatarefactored.metadatarefactored.controller

import com.turbointernational.metadatarefactored.metadatarefactored.dto.EntityTypeDto
import com.turbointernational.metadatarefactored.metadatarefactored.service.EntityTypeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@RestController
class EntityTypeController(private val entityTypeService: EntityTypeService) {
    @PostMapping("/entitytype")
    fun postEntityType(@RequestBody entityTypeDto: EntityTypeDto) {
        entityTypeService.create(entityTypeDto)
    }
    @GetMapping("/entitytype")
    fun getEntityTypes() = entityTypeService.getEntityTypes()
}