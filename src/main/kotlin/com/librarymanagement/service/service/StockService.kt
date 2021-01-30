package com.librarymanagement.service.service

import com.librarymanagement.service.model.LibraryItem
import com.librarymanagement.service.model.Sku
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.*


@Repository
interface SkuRepository : MongoRepository<Sku, String> {
    fun findByItem(libraryItem: LibraryItem): Optional<Sku>
    fun findAllWithStockGreaterThan(stock: Int = 0) : List<Sku>
}


@Service
class StockService(
        private val skuRepository: SkuRepository
) {
    fun createSku( sku: Sku): Sku {
        return if(skuRepository.findByItem(sku.item).isPresent){
            throw SkuAlreadyExistsException("SKU for this item already exists")
        }else{
            sku.id = null
            skuRepository.save(sku)
        }
    }

    fun getAllSku(): List<Sku>{
        return skuRepository.findAllWithStockGreaterThan()
    }
}

class SkuAlreadyExistsException(override val message: String) : Throwable()

