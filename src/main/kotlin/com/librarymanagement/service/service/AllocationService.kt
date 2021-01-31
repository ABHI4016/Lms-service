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
    fun findBySkuId(skuId: String): Optional<Allocation>
    fun findByAllocationsContaining(member: Member): Optional<List<Allocation>>
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

            val currentAllocations: Optional<List<Allocation>> = allocationRepository.findByAllocationsContaining(member)

            if (currentAllocations.isEmpty || currentAllocations.get().size < 2) {
                val allocation: Allocation = allocationRepository.findBySkuId(skuId)
                        .orElseGet {
                            Allocation(null, sku)
                        }
                if (!allocation.allocations.contains(member)) {
                    sku.stock--
                    skuService.update(sku)
                    allocation.allocations.add(member)
                    return allocationRepository.save(allocation)
                } else {
                    throw CantAllocateToMemberException("Can't allocate resource with skuId: $skuId to member: $memberId, either already assigned or stock unavailable")
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


            val allocation: Allocation = allocationRepository.findBySkuId(skuId)
                    .orElseGet {
                        Allocation(null, sku)
                    }
            if (allocation.allocations.contains(member)) {
                sku.stock++
                skuService.update(sku)
                allocation.allocations.remove(member)
                return allocationRepository.save(allocation)
            } else {
                throw CantDeAllocateToMemberException("Can't allocate resource with skuId: $skuId to member: $memberId, the resource is not assigned")
            }
    }
}

class CantAllocateToMemberException(override val message: String) : Throwable()
class CantDeAllocateToMemberException(override val message: String) : Throwable()

class MaxResourceAlreadyAllocatedException(override val message: String) : Throwable()
