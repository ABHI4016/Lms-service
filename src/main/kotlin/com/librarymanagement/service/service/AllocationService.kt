package com.librarymanagement.service.service

import com.librarymanagement.service.model.Allocation
import com.librarymanagement.service.model.Member
import com.librarymanagement.service.model.Sku
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Repository
interface AllocationRepository : MongoRepository<Allocation, String> {
    fun findBySkuIdAndMemberAndIsActive(skuId: String, member: Member, isActive: Boolean = true): Allocation?
    fun findByMemberAndIsActive(member: Member, isActive: Boolean = true): List<Allocation>?
    fun findByMemberIdOrderByIsActiveDesc(memberId: String): List<Allocation>?
}

@Service
class AllocationService(
        private val memberService: MemberService,
        private val allocationRepository: AllocationRepository,
        private val skuService: StockService,
) {
    companion object {
        fun generate(sku: Sku, member: Member) = Allocation(null, sku, member)
    }

    @Transactional
    fun allocate(
            memberId: String,
            skuId: String,
    ): Allocation {
        val member = memberService.findById(memberId)
        val sku = skuService.findById(skuId)

        if (sku.stock > 0) {
            val currentAllocations: List<Allocation>? = allocationRepository.findByMemberAndIsActive(member)

            if (currentAllocations.isNullOrEmpty() || currentAllocations.size < 2) {
                val allocation = allocationRepository.findBySkuIdAndMemberAndIsActive(skuId, member)

                allocation?.let {
                    throw CantAllocateToMemberException("Can't allocate resource with skuId: $skuId to member: $memberId, resource is already assigned")
                }

                sku.stock--
                skuService.update(sku)
                return allocationRepository.save(generate(sku, member))


            } else {
                throw MaxResourceAlreadyAllocatedException("The member is already allocated max permissible library resources")
            }
        } else {
            throw CantAllocateToMemberException("Can't allocate resource with skuId: $skuId to member: $memberId, either already assigned or stock unavailable")
        }
    }

    @Transactional
    fun deAllocate(
            memberId: String,
            skuId: String,
    ): Allocation {
        val member = memberService.findById(memberId)
        val sku = skuService.findById(skuId)

        allocationRepository.findBySkuIdAndMemberAndIsActive(skuId, member)?.let {
            sku.stock++
            skuService.update(sku)
            it.isActive = false
            it.lastUpdated = LocalDateTime.now()
            return allocationRepository.save(it)
        }

        throw CantDeAllocateToMemberException("Can't allocate resource with skuId: $skuId to member: $memberId, the resource is not assigned")
    }

    fun getByMemberId(memberId: String, active: Boolean): List<Allocation> {
        return allocationRepository.findByMemberIdOrderByIsActiveDesc(memberId) ?: mutableListOf()
    }
}

class CantAllocateToMemberException(override val message: String) : Throwable()
class CantDeAllocateToMemberException(override val message: String) : Throwable()
class MaxResourceAlreadyAllocatedException(override val message: String) : Throwable()
