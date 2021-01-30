package com.librarymanagement.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [EmbeddedMongoAutoConfiguration::class])
class LibraryServiceApplication

fun main(args: Array<String>) {
    runApplication<LibraryServiceApplication>(*args)
}
