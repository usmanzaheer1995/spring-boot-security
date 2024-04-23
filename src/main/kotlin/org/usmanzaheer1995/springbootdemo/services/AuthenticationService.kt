package org.usmanzaheer1995.springbootdemo.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.usmanzaheer1995.springbootdemo.models.JwtProperties
import org.usmanzaheer1995.springbootdemo.models.dtos.AuthenticationRequestDto
import org.usmanzaheer1995.springbootdemo.models.responses.AuthenticationResponse
import org.usmanzaheer1995.springbootdemo.repositories.RefreshTokenRepository
import java.util.Date

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun authenticate(authRequest: AuthenticationRequestDto): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password,
            )
        )

        val user = userDetailsService.loadUserByUsername(authRequest.email)
        
        val accessToken = generateAccessToken(user)

        val refreshToken = generateRefreshToken(user)

        refreshTokenRepository.save(token = refreshToken, userDetails = user)

        return AuthenticationResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }
    fun refreshAccessToken(token: String): String? {
        val extractedEmail = tokenService.extractEmail(token)

        return extractedEmail?.let { email ->
            val currentUserDetails = userDetailsService.loadUserByUsername(email)
            val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(token)

            if (!tokenService.isExpired(token) && currentUserDetails.username == refreshTokenUserDetails?.username) {
                generateAccessToken(currentUserDetails)
            } else {
                null
            }
        }
    }

    private fun generateRefreshToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
    )

    private fun generateAccessToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
    )

}
