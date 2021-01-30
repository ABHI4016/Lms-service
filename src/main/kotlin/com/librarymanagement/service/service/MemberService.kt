package com.librarymanagement.service.service

import com.librarymanagement.service.model.Member
import com.librarymanagement.service.resource.MemberRepo
import org.springframework.stereotype.Service

@Service
class MemberService(
        private val memberRepo: MemberRepo
) {


    fun add(member: Member): Member {
        member.id = null
        return memberRepo.insert(member)
    }

    fun all(): List<Member>{
        return memberRepo.findAll()
    }

    fun findById(id: String): Member{
        return memberRepo.findById(id).orElseThrow{
            throw MemberNotFoundException("Can't find member by id: $id")
        }
    }
}

class MemberNotFoundException(override val message: String) : Throwable()
