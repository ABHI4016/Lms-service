package com.librarymanagement.service.exception

import com.librarymanagement.service.service.*
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
    fun handleSkuAlreadyExistsException(exception: SkuAlreadyExistsException): ResponseEntity<Any> {
        logger.error(exception.message)
        return ResponseEntity(mapOf("message" to exception.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(SkuNotFoundException::class)
    fun handleSkuNotFoundException(exception: SkuNotFoundException): ResponseEntity<Any> {
        logger.error(exception.message)
        return ResponseEntity(mapOf("message" to exception.message), HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler(CantAllocateToMemberException::class)
    fun handleCantAllocateToMemberException(exception: CantAllocateToMemberException): ResponseEntity<Any> {
        logger.error(exception.message)
        return ResponseEntity(mapOf("message" to exception.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(CantDeAllocateToMemberException::class)
    fun handleCantDeAllocateToMemberException(exception: CantDeAllocateToMemberException): ResponseEntity<Any> {
        logger.error(exception.message)
        return ResponseEntity(mapOf("message" to exception.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MemberNotFoundException::class)
    fun handleMemberNotFoundException(exception: MemberNotFoundException): ResponseEntity<Any> {
        logger.error(exception.message)
        return ResponseEntity(mapOf("message" to exception.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MaxResourceAlreadyAllocatedException::class)
    fun handleMaxResourceAlreadyAllocatedException(exception: MaxResourceAlreadyAllocatedException): ResponseEntity<Any> {
        logger.error(exception.message)
        return ResponseEntity(mapOf("message" to exception.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleItemNotFoundException(exception: ItemNotFoundException): ResponseEntity<Any> {
        logger.error(exception.message)
        return ResponseEntity(mapOf("message" to exception.message), HttpStatus.BAD_REQUEST)
    }



}