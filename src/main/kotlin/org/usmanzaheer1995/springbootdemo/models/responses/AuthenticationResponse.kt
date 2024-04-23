package org.usmanzaheer1995.springbootdemo.models.responses

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String,
)
