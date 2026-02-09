package ru.itmo.hls.authmanager.presentation.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.itmo.hls.authmanager.application.dto.ApiErrorDto
import ru.itmo.hls.authmanager.domain.exception.InvalidCredentialsException
import ru.itmo.hls.authmanager.domain.exception.UserAlreadyExistsException

@RestControllerAdvice
class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(ex: UserAlreadyExistsException): ApiErrorDto {
        return ApiErrorDto(message = ex.message ?: "User already exists")
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(ex: InvalidCredentialsException): ApiErrorDto {
        return ApiErrorDto(message = ex.message ?: "Invalid credentials")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ApiErrorDto {
        return ApiErrorDto(message = ex.message ?: "Bad request")
    }
}
