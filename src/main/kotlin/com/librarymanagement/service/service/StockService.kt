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
    fun findByStockGreaterThan(stock: Int = 0): List<Sku>
}


@Service
class StockService(
        private val skuRepository: SkuRepository,
) {
    fun createSku(sku: Sku): Sku {
        return if (skuRepository.findByItem(sku.item).isPresent) {
            throw SkuAlreadyExistsException("SKU for this item already exists")
        } else {
            sku.id = null
            skuRepository.insert(sku)
        }
    }

    fun allInStockSku(): List<Sku> {
        return skuRepository.findByStockGreaterThan()
    }

    fun findById(skuId: String): Sku {
        return skuRepository.findById(skuId).orElseThrow {
            throw SkuNotFoundException("Can't find SKU for id: $skuId")
        }
    }

    fun update(sku: Sku): Sku {
        return skuRepository.save(sku)
    }
}

class SkuNotFoundException(override val message: String) : Throwable()

class SkuAlreadyExistsException(override val message: String) : Throwable()

