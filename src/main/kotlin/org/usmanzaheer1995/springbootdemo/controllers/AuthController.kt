package org.usmanzaheer1995.springbootdemo.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.usmanzaheer1995.springbootdemo.models.dtos.AuthenticationRequestDto
import org.usmanzaheer1995.springbootdemo.models.responses.AuthenticationResponse
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
}
