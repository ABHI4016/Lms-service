package com.librarymanagement.service.resource

import com.librarymanagement.service.model.Allocation
import com.librarymanagement.service.service.AllocationService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("allocation")
class AllocationResource(
        private val allocationService: AllocationService,
) {

    @PutMapping("member/{memberId}/sku/{skuId}")
    fun allocate(
            @PathVariable memberId: String,
            @PathVariable skuId: String,
    ): Allocation {
        return allocationService.allocate(memberId, skuId)
    }
}