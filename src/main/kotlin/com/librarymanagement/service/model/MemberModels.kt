package com.librarymanagement.service.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = IndividualUser::class, name = "Individual"),
)
@Document(value = "Members")
abstract class Member(
        @Id
        var id: String?,
        var name: String,
){
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is Member) return false

                if (id != other.id) return false
                if (name != other.name) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + name.hashCode()
                return result
        }
}

class IndividualUser(
        id: String?,
        name: String,
        var address: String,
) : Member(id, name)
