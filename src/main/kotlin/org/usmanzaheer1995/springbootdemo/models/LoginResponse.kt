package org.usmanzaheer1995.springbootdemo.models

data class LoginResponse(
    val token: String,
    val expiresIn: Long,
)
