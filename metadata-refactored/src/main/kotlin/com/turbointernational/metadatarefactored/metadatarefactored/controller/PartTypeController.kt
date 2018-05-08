package com.turbointernational.metadatarefactored.metadatarefactored.controller

import com.turbointernational.metadatarefactored.metadatarefactored.dto.PartTypeDto
import com.turbointernational.metadatarefactored.metadatarefactored.service.PartService
import com.turbointernational.metadatarefactored.metadatarefactored.service.PartTypeService
import org.springframework.web.bind.annotation.*

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@RestController
class PartTypeController(private val partTypeService: PartTypeService,
                         private val partService: PartService) {

    @PostMapping("/parttype")
    fun postPartType(@RequestBody partTypeDto: PartTypeDto) {
        partTypeService.create(partTypeDto)
    }

    @GetMapping("/parttype")
    fun getPartTypes() = partTypeService.getPartTypes()

    @GetMapping("/parttype/{id}/part")
    fun getPartsByPartTypes(@PathVariable("id") partId: Long) = partService.getPartsByTypeId(partId)
}