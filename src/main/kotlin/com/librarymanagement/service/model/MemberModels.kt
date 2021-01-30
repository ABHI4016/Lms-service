package com.librarymanagement.service.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = IndividualUser::class,name="Individual"),
)
@Document(value = "Members")
abstract class Member(
        @Id
        var id: String,
        var name: String
){
        lateinit var type: String
}

class IndividualUser(
        id:String,
        name: String,
        var address: String
): Member(id, name)


@Repository
interface MemberRepo: MongoRepository<Member, String>