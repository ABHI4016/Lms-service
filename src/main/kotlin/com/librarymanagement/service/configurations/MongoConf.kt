package com.librarymanagement.service.configurations

import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager

@Bean
fun transactionManager(dbFactory: MongoDatabaseFactory): MongoTransactionManager {
    return MongoTransactionManager(dbFactory)
}