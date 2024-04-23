package org.usmanzaheer1995.springbootdemo.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.usmanzaheer1995.springbootdemo.models.dtos.AuthenticationRequestDto
import org.usmanzaheer1995.springbootdemo.models.dtos.RefreshTokenRequestDto
import org.usmanzaheer1995.springbootdemo.models.responses.AuthenticationResponse
import org.usmanzaheer1995.springbootdemo.models.responses.TokenResponse
import org.usmanzaheer1995.springbootdemo.services.AuthenticationService

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
) {
    @PostMapping
    fun authenticate(@RequestBody authRequest: AuthenticationRequestDto) : AuthenticationResponse {
        return authenticationService.authenticate(authRequest)
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequestDto) : TokenResponse {
        return authenticationService.refreshAccessToken(request.token)
            ?.mapToTokenResponse()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "invalid refresh token")
    }

    fun String.mapToTokenResponse(): TokenResponse {
        return TokenResponse(
            token = this
        )
    }
}
