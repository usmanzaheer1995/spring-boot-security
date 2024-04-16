package org.usmanzaheer1995.springbootdemo.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.usmanzaheer1995.springbootdemo.models.LoginResponse
import org.usmanzaheer1995.springbootdemo.models.LoginUserDto
import org.usmanzaheer1995.springbootdemo.models.RegisterUserDto
import org.usmanzaheer1995.springbootdemo.models.User
import org.usmanzaheer1995.springbootdemo.services.AuthenticationService
import org.usmanzaheer1995.springbootdemo.services.JwtService

@RequestMapping("/auth")
@RestController
class AuthenticationController(
    private val authenticationService: AuthenticationService,
    private val jwtService: JwtService,
) {
    @PostMapping("/signup")
    fun register(
        @RequestBody registerUserDto: RegisterUserDto,
    ): ResponseEntity<User> {
        val registeredUser = authenticationService.signup(input = registerUserDto)

        return ResponseEntity.ok(registeredUser)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginUserDto: LoginUserDto,
    ): ResponseEntity<LoginResponse> {
        val authenticatedUser = authenticationService.authenticate(input = loginUserDto)
        val jwtToken = jwtService.generateToken(authenticatedUser)
        val loginResponse =
            LoginResponse(
                token = jwtToken,
                expiresIn = jwtService.expiration(jwtToken),
            )
        return ResponseEntity.ok(loginResponse)
    }
}
