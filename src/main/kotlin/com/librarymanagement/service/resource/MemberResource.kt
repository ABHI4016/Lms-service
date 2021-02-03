package com.librarymanagement.service.resource

import com.librarymanagement.service.model.Member
import com.librarymanagement.service.service.MemberService
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("member")
class MemberResource(
        private val memberService: MemberService,
) {

    @PostMapping
    fun add(@RequestBody member: Member): Member {
        return memberService.add(member)
    }

    @PostMapping("login")
    fun login(@RequestBody loginRequest: LoginRequest ): Member {
        return memberService.login(loginRequest.userName, loginRequest.password)
    }

    @GetMapping("/all")
    fun allMembers(): List<Member> {
        return memberService.all()
    }


}

class LoginRequest(val userName: String, val password: String)