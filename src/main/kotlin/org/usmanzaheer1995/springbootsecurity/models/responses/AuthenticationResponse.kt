package org.usmanzaheer1995.springbootsecurity.models.responses

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String,
)
