package org.usmanzaheer1995.springbootdemo.models.dtos

data class AuthenticationRequestDto(
    val email: String,
    val password: String,
)
