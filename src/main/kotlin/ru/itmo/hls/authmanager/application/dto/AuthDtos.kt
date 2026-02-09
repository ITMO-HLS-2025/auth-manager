package ru.itmo.hls.authmanager.application.dto

import ru.itmo.hls.authmanager.domain.model.UserRole

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterUserRequest(
    val username: String,
    val password: String,
    val role: UserRole,
    val theatreId: Long? = null
)

data class UserResponse(
    val id: Long,
    val username: String,
    val role: UserRole,
    val theatreId: Long?
)

data class AuthResponse(
    val token: String,
    val user: UserResponse
)
