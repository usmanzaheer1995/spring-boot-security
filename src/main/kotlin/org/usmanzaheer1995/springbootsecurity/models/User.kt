package org.usmanzaheer1995.springbootsecurity.models

import org.usmanzaheer1995.springbootsecurity.models.responses.UserResponse
import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val username: String,
    val password: String,
    val role: UserRole,
) {
    fun toResponse() =
        UserResponse(
            id = this.id,
            email = this.email,
        )
}
