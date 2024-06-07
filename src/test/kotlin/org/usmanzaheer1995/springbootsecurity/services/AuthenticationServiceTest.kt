package org.usmanzaheer1995.springbootsecurity.services

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.usmanzaheer1995.springbootsecurity.models.JwtProperties
import org.usmanzaheer1995.springbootsecurity.models.UserRole
import org.usmanzaheer1995.springbootsecurity.models.dtos.AuthenticationRequestDto
import org.usmanzaheer1995.springbootsecurity.repositories.RefreshTokenRepository

@SpringBootTest(classes = [AuthenticationService::class])
class AuthenticationServiceTest(
    @MockBean private val authenticationManager: AuthenticationManager,
    @MockBean private val userDetailsService: CustomUserDetailsService,
    @MockBean private val tokenService: TokenService,
    @MockBean private val jwtProperties: JwtProperties,
    @MockBean private val refreshTokenRepository: RefreshTokenRepository,
): FunSpec({
    extension(SpringExtension)

    val authenticationService = AuthenticationService(
        authManager = authenticationManager,
        userDetailsService = userDetailsService,
        tokenService = tokenService,
        jwtProperties = jwtProperties,
        refreshTokenRepository = refreshTokenRepository,
    )

    beforeEach {
        println("hello from $it")
    }

    test("authenticate should return AuthenticationResponse") {
        val authRequest = AuthenticationRequestDto("test@example.com", "password")
        val userDetails = User.builder()
            .username("test@example.com")
            .password("password")
            .roles(UserRole.USER.name)
            .build()

        val accessToken = "access-token"
        val refreshToken = "refresh-token"

        every { authenticationManager.authenticate(any()) } returns mockk<Authentication>()
        every { userDetailsService.loadUserByUsername(authRequest.email) } returns userDetails
        every { tokenService.generate(any(), any()) } returnsMany listOf(accessToken, refreshToken)
        every { refreshTokenRepository.save(any(), any()) } returns Unit
        every { jwtProperties.accessTokenExpiration } returns 3600000L // 1 hour
        every { jwtProperties.refreshTokenExpiration } returns 86400000L // 1 day

        val response = authenticationService.authenticate(authRequest)

        response.accessToken shouldBe accessToken
        response.refreshToken shouldBe refreshToken

        verify { authenticationManager.authenticate(match {
            it is UsernamePasswordAuthenticationToken &&
                    it.name == authRequest.email &&
                    it.credentials == authRequest.password
        }) }
        verify { userDetailsService.loadUserByUsername(authRequest.email) }
        verify { tokenService.generate(userDetails, any()) }
        verify { refreshTokenRepository.save(refreshToken, userDetails) }
    }

    test("refreshAccessToken should return new access token when refresh token is valid") {
        val refreshToken = "valid-refresh-token"
        val email = "test@example.com"
        val userDetails = mockk<UserDetails>()
        val newAccessToken = "new-access-token"

        every { tokenService.extractEmail(refreshToken) } returns email
        every { userDetailsService.loadUserByUsername(email) } returns userDetails
        every { refreshTokenRepository.findUserDetailsByToken(refreshToken) } returns userDetails
        every { tokenService.isExpired(refreshToken) } returns false
        every { userDetails.username } returns email
        every { tokenService.generate(userDetails, any()) } returns newAccessToken

        val result = authenticationService.refreshAccessToken(refreshToken)

        result shouldBe newAccessToken

        verify { tokenService.extractEmail(refreshToken) }
        verify { userDetailsService.loadUserByUsername(email) }
        verify { refreshTokenRepository.findUserDetailsByToken(refreshToken) }
        verify { tokenService.isExpired(refreshToken) }
        verify { tokenService.generate(userDetails, any()) }
    }

    test("refreshAccessToken should return null when refresh token is expired") {
        val refreshToken = "expired-refresh-token"
        val email = "test@example.com"

        every { tokenService.extractEmail(refreshToken) } returns email
        every { userDetailsService.loadUserByUsername(email) } returns mockk()
        every { refreshTokenRepository.findUserDetailsByToken(refreshToken) } returns mockk()
        every { tokenService.isExpired(refreshToken) } returns true

        val result = authenticationService.refreshAccessToken(refreshToken)

        result shouldBe null

        verify { tokenService.extractEmail(refreshToken) }
        verify { userDetailsService.loadUserByUsername(email) }
        verify { refreshTokenRepository.findUserDetailsByToken(refreshToken) }
        verify { tokenService.isExpired(refreshToken) }
    }
})
