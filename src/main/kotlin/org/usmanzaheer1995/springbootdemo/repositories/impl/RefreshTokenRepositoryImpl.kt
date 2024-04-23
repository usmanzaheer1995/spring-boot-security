package org.usmanzaheer1995.springbootdemo.repositories.impl

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import org.usmanzaheer1995.springbootdemo.repositories.RefreshTokenRepository

@Repository
class RefreshTokenRepositoryImpl : RefreshTokenRepository {
    private val tokens = mutableMapOf<String, UserDetails>()
    override fun findUserDetailsByToken(token: String): UserDetails? {
        return tokens[token]
    }

    override fun save(token: String, userDetails: UserDetails) {
        tokens[token] = userDetails
    }
}
