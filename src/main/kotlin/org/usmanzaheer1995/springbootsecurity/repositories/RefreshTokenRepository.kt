package org.usmanzaheer1995.springbootsecurity.repositories

import org.springframework.security.core.userdetails.UserDetails

interface RefreshTokenRepository {
    fun findUserDetailsByToken(token: String) : UserDetails?

    fun save(token: String, userDetails: UserDetails)
}
