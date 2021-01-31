package com.librarymanagement.service.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = Book::class, name = "Book")
)
@Document("LibraryItems")
open class LibraryItem(
        @Id
        var id: String?,
        var name: String,
)

class Book(
        var author: String,
        id: String?,
        name: String,
) : LibraryItem(id, name)

@Document("Sku")
data class Sku(
        @Id
        var id: String?,
        @DBRef
        val item: LibraryItem,
) {
    var stock: Int = 0

    @Version
    var version: Long = 0
}

@Repository
interface ItemRepository : MongoRepository<LibraryItem, String>
