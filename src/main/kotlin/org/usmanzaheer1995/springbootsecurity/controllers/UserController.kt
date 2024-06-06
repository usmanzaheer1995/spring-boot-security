package org.usmanzaheer1995.springbootsecurity.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.usmanzaheer1995.springbootsecurity.models.dtos.UserDto
import org.usmanzaheer1995.springbootsecurity.models.responses.UserResponse
import org.usmanzaheer1995.springbootsecurity.services.UserService
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    fun create(
        @RequestBody user: UserDto,
    ): UserResponse? {
        val createdUser = userService.createUser(user.toModel())

        return createdUser
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create a user")
    }

    @GetMapping
    fun listAll(): List<UserResponse> {
        return userService.findAll().map { it.toResponse() }
    }

    @GetMapping("/{uuid}")
    fun getUser(
        @PathVariable uuid: UUID,
    ): UserResponse {
        return userService
            .findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }

    @DeleteMapping("/{uuid}")
    fun deleteUser(
        @PathVariable uuid: UUID,
    ): ResponseEntity<Boolean> {
        val success = userService.deleteUser(uuid)

        return if (success) {
            ResponseEntity.noContent().build()
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
    }
}
