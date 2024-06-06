package org.usmanzaheer1995.springbootsecurity.repositories.impl

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import org.usmanzaheer1995.springbootsecurity.models.User
import org.usmanzaheer1995.springbootsecurity.models.UserRole
import org.usmanzaheer1995.springbootsecurity.repositories.UserRepository
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val encoder: PasswordEncoder,
) : UserRepository {
    private val users =
        mutableListOf(
            User(
                id = UUID.randomUUID(),
                email = "john@doe.com",
                username = "john.doe",
                password = encoder.encode("password"),
                role = UserRole.USER,
            ),
            User(
                id = UUID.randomUUID(),
                email = "jane@doe.com",
                username = "jane.doe",
                password = encoder.encode("password"),
                role = UserRole.ADMIN,
            ),
        )

    override fun findByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override fun save(user: User): Boolean {
        val updatedUser = user.copy(password = encoder.encode(user.password))
        return users.add(updatedUser)
    }

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
