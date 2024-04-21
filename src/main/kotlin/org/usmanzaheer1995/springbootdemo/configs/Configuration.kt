package org.usmanzaheer1995.springbootdemo.configs

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.usmanzaheer1995.springbootdemo.models.JwtProperties
import org.usmanzaheer1995.springbootdemo.repositories.UserRepository
import org.usmanzaheer1995.springbootdemo.services.CustomUserDetailsService

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class Configuration {
    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return CustomUserDetailsService(userRepository)
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(userRepository: UserRepository): AuthenticationProvider {
        return DaoAuthenticationProvider()
            .also {
                it.setUserDetailsService(userDetailsService(userRepository))
                it.setPasswordEncoder(encoder())
            }
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}
