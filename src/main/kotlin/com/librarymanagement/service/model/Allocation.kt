package com.librarymanagement.service.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Allocation(
        @Id
        var id: String?,
        @DBRef
        val sku: Sku,
) {
    lateinit var allocations :MutableList<Member>
}