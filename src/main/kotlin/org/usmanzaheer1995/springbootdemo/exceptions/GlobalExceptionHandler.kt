package org.usmanzaheer1995.springbootdemo.exceptions

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.security.authentication.AccountStatusException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.SignatureException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleSecurityException(exception: Exception): ProblemDetail {
        var errorDetail: ProblemDetail? = null

        exception.printStackTrace()

        if (exception is BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.message.toString())
            errorDetail.properties = mapOf("description" to "The username or password is incorrect")
            return errorDetail
        }

        if (exception is AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.message.toString())
            errorDetail.properties = mapOf("description" to "The account is locked")
            return errorDetail
        }

        if (exception is AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.message.toString())
            errorDetail.properties = mapOf("description" to "You are not authorized to access this resource")
            return errorDetail
        }

        if (exception is SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.message.toString())
            errorDetail.properties = mapOf("description" to "The JWT signature is invalid")
            return errorDetail
        }

        if (exception is ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.message.toString())
            errorDetail.properties = mapOf("description" to "The JWT token has expired")
            return errorDetail
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.message.toString())
            errorDetail.properties = mapOf("description" to "An unexpected internal error occurred")
        }

        return errorDetail
    }
}
