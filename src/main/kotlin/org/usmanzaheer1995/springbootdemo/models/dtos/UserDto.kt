package org.usmanzaheer1995.springbootdemo.models.dtos

import org.usmanzaheer1995.springbootdemo.models.User
import org.usmanzaheer1995.springbootdemo.models.UserRole
import java.util.UUID

data class UserDto(
    val email: String,
    val username: String,
    val password: String,
) {
    fun toModel() =
        User(
            email = this.email,
            username = this.username,
            password = this.password,
            id = UUID.randomUUID(),
            role = UserRole.USER,
        )
}
