package com.librarymanagement.service.resource

import com.librarymanagement.service.model.LibraryItem
import com.librarymanagement.service.service.LibraryItemService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("item")
class LibraryItemResource(
        private val itemService: LibraryItemService,
) {

    @PostMapping
    fun create(@RequestBody item: LibraryItem): LibraryItem {
        return itemService.save(item)
    }
}