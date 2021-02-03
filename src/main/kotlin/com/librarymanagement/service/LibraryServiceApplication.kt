package com.librarymanagement.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication(exclude = [EmbeddedMongoAutoConfiguration::class])
class LibraryServiceApplication

fun main(args: Array<String>) {
    runApplication<LibraryServiceApplication>(*args)
}
