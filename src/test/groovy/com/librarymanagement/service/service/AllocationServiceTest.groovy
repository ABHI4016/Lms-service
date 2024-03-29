package com.librarymanagement.service.service

import com.librarymanagement.service.model.Allocation
import com.librarymanagement.service.model.Book
import com.librarymanagement.service.model.IndividualUser
import com.librarymanagement.service.model.Sku
import spock.lang.Specification

class AllocationServiceTest extends Specification {

    AllocationService allocationService
    MemberService mockMemberService
    AllocationRepository mockAllocationRepository
    StockService mockStockService

    def setup() {
        mockMemberService = Mock(MemberService)
        mockAllocationRepository = Mock(AllocationRepository)
        mockStockService = Mock(StockService)

        allocationService = new AllocationService(mockMemberService, mockAllocationRepository, mockStockService)
    }


    def "allocate: Should allocate a resource if allocation criteria is met"() {
        given:
        def member = new IndividualUser("Member-id-1", "John Doe", "Mock Street", "mid1","password")
        def sku = new Sku("Sku-id-1", new Book("JK Rowling", "Harry-potter-1", "Harry Potter"))
        sku.stock = 2

        def allocation = new Allocation("alloca-1", sku, member)
        when:
        def response = allocationService.allocate("Member-id-1", "Sku-id-1")

        then:
        1 * mockMemberService.findById("Member-id-1") >> member
        1 * mockStockService.findById("Sku-id-1") >> sku
        1 * mockAllocationRepository.findByMemberAndIsActive(member, true) >> []
        1 * mockAllocationRepository.findBySkuIdAndMemberAndIsActive("Sku-id-1", member, true) >> null
        1 * mockStockService.update(sku) >> sku
        1 * mockAllocationRepository.save(_ as Allocation) >> allocation

        response
        response.member == member
        response.sku == sku
    }

    def "allocate: Should throw MaxResourceAllocatedException if the user has max allowed allocations"() {
        given:
        def member = new IndividualUser("Member-id-1", "John Doe", "Mock Street", "mid1","password")
        def sku = new Sku("Sku-id-1", new Book("JK Rowling", "Harry-potter-1", "Harry Potter"))
        sku.stock = 2

        def allocation1 = new Allocation("alloca-1", sku, member)
        def allocation2 = new Allocation("alloca-2", sku, member)

        when:
        allocationService.allocate("Member-id-1", "Sku-id-1")

        then:
        1 * mockMemberService.findById("Member-id-1") >> member
        1 * mockStockService.findById("Sku-id-1") >> sku
        1 * mockAllocationRepository.findByMemberAndIsActive(member, true) >> [allocation1, allocation2]


        thrown MaxResourceAlreadyAllocatedException
    }

    def "allocate: Should throw CanAllocateToMemberException if the stock is unavailable"() {
        given:
        def member = new IndividualUser("Member-id-1", "John Doe", "Mock Street", "mid1","password")
        def sku = new Sku("Sku-id-1", new Book("JK Rowling", "Harry-potter-1", "Harry Potter"))
        sku.stock = 0

        when:
        allocationService.allocate("Member-id-1", "Sku-id-1")

        then:
        1 * mockMemberService.findById("Member-id-1") >> member
        1 * mockStockService.findById("Sku-id-1") >> sku


        thrown CantAllocateToMemberException
    }

    def "allocate: Should throw CanAllocateToMemberException if the resource is already allocated"() {
        given:
        def member = new IndividualUser("Member-id-1", "John Doe", "Mock Street", "mid1","password")
        def sku = new Sku("Sku-id-1", new Book("JK Rowling", "Harry-potter-1", "Harry Potter"))
        sku.stock = 2
        def allocation = new Allocation("alloca-1", sku, member)

        when:
        allocationService.allocate("Member-id-1", "Sku-id-1")

        then:
        1 * mockMemberService.findById("Member-id-1") >> member
        1 * mockStockService.findById("Sku-id-1") >> sku
        1 * mockAllocationRepository.findByMemberAndIsActive(member, true) >> [allocation]
        1 * mockAllocationRepository.findBySkuIdAndMemberAndIsActive("Sku-id-1", member, true) >> allocation


        thrown CantAllocateToMemberException
    }
}
