package com.librarymanagement.service.service

import com.librarymanagement.service.model.ItemRepository
import com.librarymanagement.service.model.LibraryItem
import org.springframework.stereotype.Service

@Service
class LibraryItemService(
        private val itemRepository: ItemRepository,
) {

    fun save(item: LibraryItem): LibraryItem {
        item.id = null
        return itemRepository.save(item)
    }

    fun findById(id: String): LibraryItem {
        return itemRepository.findById(id).orElseThrow {
            throw ItemNotFoundException("Cant find the resource with id: $id")
        }
    }

    fun findAll(): List<LibraryItem> {
        return itemRepository.findAll()
    }
}

class ItemNotFoundException(override val message: String) : Throwable() {

}
