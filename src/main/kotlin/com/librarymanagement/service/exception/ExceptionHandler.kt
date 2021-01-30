package com.librarymanagement.service.exception

import com.librarymanagement.service.service.SkuAlreadyExistsException
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(SkuAlreadyExistsException::class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun handleCardException(exception: SkuAlreadyExistsException): ResponseEntity<Any> {
        logger.error(exception.message, exception)
        return ResponseEntity(mapOf("message" to exception.message), HttpStatus.BAD_REQUEST)
    }

}