package com.librarymanagement.service.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Document
data class Allocation(
        @Id
        var id: String?,
        @DBRef
        val sku: Sku,
        @DBRef
        val member: Member
){
    var isActive = true
    var dateOfReservation: LocalDateTime  = LocalDateTime.now()
    var lastUpdated: LocalDateTime = LocalDateTime.now()
}