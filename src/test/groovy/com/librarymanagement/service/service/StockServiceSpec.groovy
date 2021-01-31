package com.librarymanagement.service.service

import com.librarymanagement.service.model.Book
import com.librarymanagement.service.model.LibraryItem
import com.librarymanagement.service.model.Sku
import spock.lang.Specification

class StockServiceSpec extends Specification {
    StockService stockService
    SkuRepository skuRepository

    def setup() {
        skuRepository = Mock(SkuRepository)
        stockService = new StockService(skuRepository)
    }

    def "Should be able to create a SKU for a new library item"() {
        given:
        def item = new Book("JK Rowling", "Item-1", "Harry Potter")
        def sku = new Sku("sku-1", item)
        sku.stock = 1
        when:
        def response = stockService.createSku(sku)
        then:
        1 * skuRepository.findByItem(item) >> Optional.ofNullable(null)
        1 * skuRepository.save(sku) >> sku
        response == sku
    }

    def "Should throw SkuAlreadyExistsException when a SKU already exists for an item"() {
        given:
        def item = new Book("JK Rowling", "Item-1", "Harry Potter")
        def sku = new Sku("sku-1", item)
        sku.stock = 1
        when:
        stockService.createSku(sku)
        then:
        1 * skuRepository.findByItem(_ as LibraryItem) >> Optional.ofNullable(sku)
        SkuAlreadyExistsException exception = thrown()
        exception.message == "SKU for this item already exists"
    }

    def "Should return all in stock sku"() {
        given:
        def item1 = new Book("JK Rowling", "Item-1", "Harry Potter")
        def sku1 = new Sku("sku-1", item1)
        sku.stock = 1

        def item2 = new Book("JK Rowling", "Item-2", "Harry Potter")
        def sku2 = new Sku("sku-2", item2)
        sku.stock = 1


        def skus = [sku1, sku2]

        when:
        def response = stockService.allInStockSku()

        then:
        1 * skuRepository.findByStockGreaterThan(0) >> skus
        response.size() == 2
    }

    def "Should return an empty response in case no in stock sku is found"() {
        when:
        def response = stockService.allInStockSku()

        then:
        1 * skuRepository.findByStockGreaterThan(0) >> []
        response.size() == 0
    }
}
