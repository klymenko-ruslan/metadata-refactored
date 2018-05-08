package com.turbointernational.metadatarefactored.metadatarefactored.controller

import com.turbointernational.metadatarefactored.metadatarefactored.service.PartService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@RestController
class PartController(private val partService: PartService) {
    @GetMapping("/part")
    fun getParts() = partService.getParts()
}