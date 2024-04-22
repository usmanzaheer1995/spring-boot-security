package org.usmanzaheer1995.springbootdemo.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.usmanzaheer1995.springbootdemo.models.JwtProperties
import org.usmanzaheer1995.springbootdemo.models.dtos.AuthenticationRequestDto
import org.usmanzaheer1995.springbootdemo.models.responses.AuthenticationResponse
import java.util.Date

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
) {
    fun authenticate(authRequest: AuthenticationRequestDto): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password,
            )
        )

        val user = userDetailsService.loadUserByUsername(authRequest.email)
        val accessToken = tokenService.generate(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        )

        return AuthenticationResponse(
            accessToken = accessToken,
        )
    }
}
