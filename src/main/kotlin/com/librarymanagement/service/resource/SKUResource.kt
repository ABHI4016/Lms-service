package com.librarymanagement.service.resource

import com.librarymanagement.service.model.Sku
import com.librarymanagement.service.service.StockService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("sku")
class SKUResource(
        private val stockService: StockService,
) {

    @PostMapping
    fun create(@RequestBody sku: Sku): Sku {
        return stockService.createSku(sku)
    }

    @GetMapping("/all")
    fun findAll(): List<Sku> {
        return stockService.getAllSku()
    }
}