package com.librarymanagement.service.service

import com.librarymanagement.service.model.Allocation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import java.util.*


@Repository
interface AllocationRepository: MongoRepository<Allocation, String>{
    fun findBySkuId(skuId: String): Optional<Allocation>
}

@RestController
@RequestMapping("allocation")
class AllocationService(
        private val memberService: MemberService,
        private val allocationRepository: AllocationRepository,
        private val skuService: StockService
) {

    @PutMapping("member/{memberId}/sku/{skuId}")
    fun allocate(
            @PathVariable memberId: String,
            @PathVariable skuId: String
    ){
        val member = memberService.findById(memberId)
        val sku = skuService.findById(skuId)
        val allocation = allocationRepository.findBySkuId(skuId).orElse{
            var allocation = Allocation(null,)
        }
    }
}