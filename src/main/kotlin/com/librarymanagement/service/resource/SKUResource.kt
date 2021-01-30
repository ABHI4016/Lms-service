package com.librarymanagement.service.resource

import com.librarymanagement.service.model.Sku
import com.librarymanagement.service.service.StockService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("sku")
class SKUResource(
        private val stockService: StockService,
) {

    @PostMapping
    fun create(@RequestBody sku: Sku): Sku {
        return stockService.createSku(sku)
    }
}