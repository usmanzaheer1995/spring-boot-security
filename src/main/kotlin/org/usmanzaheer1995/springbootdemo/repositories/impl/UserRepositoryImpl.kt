package org.usmanzaheer1995.springbootdemo.repositories.impl

import org.springframework.stereotype.Repository
import org.usmanzaheer1995.springbootdemo.models.User
import org.usmanzaheer1995.springbootdemo.models.UserRole
import org.usmanzaheer1995.springbootdemo.repositories.UserRepository
import java.util.UUID

@Repository
class UserRepositoryImpl : UserRepository {
    private val users =
        mutableListOf(
            User(
                id = UUID.randomUUID(),
                email = "john@doe.com",
                username = "john.doe",
                password = "password",
                role = UserRole.USER,
            ),
            User(
                id = UUID.randomUUID(),
                email = "jane@doe.com",
                username = "jane.doe",
                password = "password",
                role = UserRole.ADMIN,
            ),
        )

    override fun findByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override fun save(user: User): Boolean = users.add(user)

    override fun findAll(): List<User> {
        return users
    }

    override fun findByUUID(id: UUID): User? {
        return users.firstOrNull { it.id == id }
    }

    override fun deletedByUUID(id: UUID): Boolean {
        val foundUser = findByUUID(id)

        return foundUser?.let {
            users.remove(it)
        } ?: false
    }
}
