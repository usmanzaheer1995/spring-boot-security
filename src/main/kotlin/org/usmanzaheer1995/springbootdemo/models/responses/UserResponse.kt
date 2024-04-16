package org.usmanzaheer1995.springbootdemo.models.responses

import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
)
