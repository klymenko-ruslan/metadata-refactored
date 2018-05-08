package com.turbointernational.metadatarefactored.metadatarefactored.controller

import com.turbointernational.metadatarefactored.metadatarefactored.migration.MigrationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

/**
 * ruslan.klymenko@zorallabs.com 08.05.18
 */
@RestController
class MigrationController(private val migrationService: MigrationService) {
    @PostMapping("migrate")
    fun migrate() {
        migrationService.migrate()
    }
}