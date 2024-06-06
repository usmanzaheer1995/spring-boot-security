package org.usmanzaheer1995.springbootsecurity.models.dtos

data class AuthenticationRequestDto(
    val email: String,
    val password: String,
)
