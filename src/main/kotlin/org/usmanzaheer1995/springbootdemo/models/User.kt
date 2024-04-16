package org.usmanzaheer1995.springbootdemo.models

import org.jooq.generated.persistence.db.tables.records.UsersRecord
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

data class User(
    val id: Int,
    val role: UserRole,
    val fullName: String,
    val email: String,
    private val password: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(GrantedAuthority { role.name })
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        fun fromDto(dto: RegisterUserDto): User {
            return User(
                id = 0,
                role = dto.role,
                fullName = dto.fullName,
                email = dto.email,
                password = dto.password,
                createdAt = dto.createdAt,
                updatedAt = dto.updatedAt,
            )
        }

        fun fromUserRecord(record: UsersRecord): User {
            return User(
                id = record.id ?: 0,
                role = UserRole.valueOf(record.userRole),
                fullName = record.fullName,
                email = record.email,
                password = record.password,
                createdAt = record.createdAt,
                updatedAt = record.updatedAt,
            )
        }
    }
}
