package org.usmanzaheer1995.springbootsecurity.services

import org.springframework.stereotype.Service
import org.usmanzaheer1995.springbootsecurity.models.User
import org.usmanzaheer1995.springbootsecurity.repositories.UserRepository
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun createUser(user: User): User? {
        return if (userRepository.save(user)) user else null
    }

    fun findByUUID(uuid: UUID): User? {
        return userRepository.findByUUID(uuid)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun deleteUser(uuid: UUID): Boolean {
        return userRepository.deletedByUUID(uuid)
    }
}
