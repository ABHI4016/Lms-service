package com.librarymanagement.service.service

import com.librarymanagement.service.model.Book
import com.librarymanagement.service.model.ItemRepository
import spock.lang.Specification

class LibraryItemServiceTest extends Specification {
    LibraryItemService itemService
    ItemRepository itemRepository

    def setup() {
        itemRepository = Mock(ItemRepository)
        itemService = new LibraryItemService(itemRepository)
    }

    def "Should be able add a library item to the library"() {
        given:
        def item = new Book("JK Rowling", "Item-1", "Harry Potter")
        when:
        def response = itemService.save(item)
        then:
        response
        1 * itemRepository.save(item) >> item
        response.id == null
    }

    def "Should be able to retrieve all items"() {
        given:
        def item1 = new Book("JK Rowling", "Item-1", "Harry Potter")
        def item2 = new Book("JK Rowling", "Item-1", "Harry Potter")
        def item3 = new Book("JK Rowling", "Item-1", "Harry Potter")
        when:
        def response = itemService.findAll()
        then:
        1 * itemRepository.findAll() >> [item1, item2, item3]
        response.size() == 3
    }

    def "Should be able to retrieve an item by id"() {
        given:
        def item = new Book("JK Rowling", "Item-1", "Harry Potter")
        when:
        def response = itemService.findById("item-1")

        then:
        1 * itemRepository.findById("item-1") >> Optional.of(item)
        item
    }

    def "Should throw item not found exception for unavailable resource"() {
        when:
        itemService.findById("item-2")
        then:
        1 * itemRepository.findById("item-2") >> Optional.ofNullable(null)
        thrown ItemNotFoundException
    }
}
