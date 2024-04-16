package org.usmanzaheer1995.springbootdemo.services

import org.springframework.stereotype.Service
import org.usmanzaheer1995.springbootdemo.repositories.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun getAllUsers() = userRepository.findAll()
}
