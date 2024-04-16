package org.usmanzaheer1995.springbootdemo.models

import java.time.LocalDateTime

data class RegisterUserDto(
    val fullName: String,
    val email: String,
    val password: String,
    val role: UserRole,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
