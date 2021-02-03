package com.librarymanagement.service.resource

import com.librarymanagement.service.model.Allocation
import com.librarymanagement.service.service.AllocationService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("allocation")
class AllocationResource(
        private val allocationService: AllocationService,
) {

    @GetMapping("member/{memberId}")
    fun getByMemberId(
            @PathVariable memberId: String,
            @RequestParam(value = "isActive", defaultValue = true.toString(), required = false) isActive: Boolean = true
    ): List<Allocation>{
        return  allocationService.getByMemberId(memberId, isActive)
    }

    @PutMapping("member/{memberId}/sku/{skuId}")
    fun allocate(
            @PathVariable memberId: String,
            @PathVariable skuId: String,
    ): Allocation {
        return allocationService.allocate(memberId, skuId)
    }

    @CrossOrigin()
    @DeleteMapping("member/{memberId}/sku/{skuId}")
    fun deAllocate(
            @PathVariable memberId: String,
            @PathVariable skuId: String,
    ): Allocation {
        return allocationService.deAllocate(memberId, skuId)
    }

}