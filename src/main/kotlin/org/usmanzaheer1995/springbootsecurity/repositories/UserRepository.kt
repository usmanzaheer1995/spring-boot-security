package org.usmanzaheer1995.springbootsecurity.repositories

import org.usmanzaheer1995.springbootsecurity.models.User
import java.util.UUID

interface UserRepository {
    fun findByEmail(email: String): User?

    fun save(user: User): Boolean

    fun findAll(): List<User>

    fun findByUUID(id: UUID): User?

    fun deletedByUUID(id: UUID): Boolean
}
