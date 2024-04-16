package org.usmanzaheer1995.springbootdemo.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.usmanzaheer1995.springbootdemo.models.User
import org.usmanzaheer1995.springbootdemo.services.UserService

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/me")
    fun authenticatedUser(): ResponseEntity<User> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as User
        return ResponseEntity.ok(user)
    }

    @GetMapping("/")
    fun allUsers(): ResponseEntity<List<User>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }
}
