package com.librarymanagement.service.service

import com.librarymanagement.service.model.Member
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service


@Repository
interface MemberRepo : MongoRepository<Member, String>{
    fun findByUserNameAndPassword(userName: String, password: String): Member?
}

@Service
class MemberService(
        private val memberRepo: MemberRepo,
) {


    fun login(userName: String, password: String): Member {
        memberRepo.findByUserNameAndPassword(userName, password)?.let{
            it.password = ""
            return it
        }

        throw MemberNotFoundException("Cant Login with those credentials")
    }

    fun add(member: Member): Member {
        member.id = null
        return memberRepo.insert(member)
    }

    fun all(): List<Member> {
        return memberRepo.findAll()
    }

    fun findById(id: String): Member {
        val member =  memberRepo.findById(id).orElseThrow {
            throw MemberNotFoundException("Can't find member by id: $id")
        }
        member.password = ""
        return member
    }
}

class MemberNotFoundException(override val message: String) : Throwable()
