package com.librarymanagement.service.resource

import com.librarymanagement.service.model.Member
import com.librarymanagement.service.service.MemberService
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*


@Repository
interface MemberRepo : MongoRepository<Member, String>

@RestController
@RequestMapping("member")
class MemberResource(
        private val memberService: MemberService,
) {

    @PostMapping
    fun add(@RequestBody member: Member): Member {
        return memberService.add(member)
    }

    @GetMapping("/all")
    fun allMembers(): List<Member> {
        return memberService.all()
    }


}