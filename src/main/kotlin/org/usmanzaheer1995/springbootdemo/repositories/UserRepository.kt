package org.usmanzaheer1995.springbootdemo.repositories

import org.usmanzaheer1995.springbootdemo.models.RegisterUserDto
import org.usmanzaheer1995.springbootdemo.models.User

interface UserRepository {
    fun findByEmail(email: String): User?

    fun save(registerUserDto: RegisterUserDto): User

    fun findAll(): List<User>
}
