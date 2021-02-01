package com.librarymanagement.service.service

import com.librarymanagement.service.model.Allocation
import com.librarymanagement.service.model.Member
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
interface AllocationRepository : MongoRepository<Allocation, String> {
    fun findBySkuIdAndMemberAndIsActive(skuId: String, member: Member, isActive: Boolean = true): Optional<Allocation>
    fun findByMemberAndIsActive(member: Member, isActive: Boolean = true): Optional<List<Allocation>>
}

@Service
class AllocationService(
        private val memberService: MemberService,
        private val allocationRepository: AllocationRepository,
        private val skuService: StockService,
) {

    @Transactional
    fun allocate(
            memberId: String,
            skuId: String,
    ): Allocation {
        val member = memberService.findById(memberId)
        val sku = skuService.findById(skuId)

        if (sku.stock > 0) {
            val currentAllocations: Optional<List<Allocation>> = allocationRepository.findByMemberAndIsActive(member)

            if (currentAllocations.isEmpty || currentAllocations.get().size < 2) {
                val allocation = allocationRepository.findBySkuIdAndMemberAndIsActive(skuId, member)
                if (allocation.isEmpty) {
                    sku.stock--
                    skuService.update(sku)
                    val newAllocation = Allocation(null, sku, member)
                    return allocationRepository.save(newAllocation)
                } else {
                    throw CantAllocateToMemberException("Can't allocate resource with skuId: $skuId to member: $memberId, resource is already assigned")
                }

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

        val allocationOptional = allocationRepository.findBySkuIdAndMemberAndIsActive(skuId, member)
        if (allocationOptional.isPresent) {
            sku.stock++
            skuService.update(sku)
            val allocation = allocationOptional.get()
            allocation.isActive = false
            return allocationRepository.save(allocation)
        } else {
            throw CantDeAllocateToMemberException("Can't allocate resource with skuId: $skuId to member: $memberId, the resource is not assigned")
        }
    }
}

class CantAllocateToMemberException(override val message: String) : Throwable()
class CantDeAllocateToMemberException(override val message: String) : Throwable()

class MaxResourceAlreadyAllocatedException(override val message: String) : Throwable()
