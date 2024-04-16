package org.usmanzaheer1995.springbootdemo.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.usmanzaheer1995.springbootdemo.models.LoginUserDto
import org.usmanzaheer1995.springbootdemo.models.RegisterUserDto
import org.usmanzaheer1995.springbootdemo.models.User
import org.usmanzaheer1995.springbootdemo.models.UserRole
import org.usmanzaheer1995.springbootdemo.repositories.UserRepository

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
) {
    private fun mapStringToUserRole(role: String): UserRole {
        return when (role) {
            "ADMIN" -> UserRole.ADMIN
            "USER" -> UserRole.USER
            else -> throw IllegalArgumentException("Invalid role")
        }
    }

    fun signup(input: RegisterUserDto): User {
        val user =
            RegisterUserDto(
                fullName = input.fullName,
                email = input.email,
                password = passwordEncoder.encode(input.password),
                role = input.role,
                createdAt = input.createdAt,
                updatedAt = input.updatedAt,
            )
        return userRepository.save(user)
    }

    fun authenticate(input: LoginUserDto): User {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(input.email, input.password),
        )

        return userRepository.findByEmail(input.email)
            ?: throw IllegalArgumentException("User not found with email: ${input.email}")
    }
}
