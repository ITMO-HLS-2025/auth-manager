package ru.itmo.hls.authmanager.presentation.rest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import ru.itmo.hls.authmanager.application.dto.AuthResponse
import ru.itmo.hls.authmanager.application.dto.LoginRequest
import ru.itmo.hls.authmanager.application.dto.RegisterUserRequest
import ru.itmo.hls.authmanager.application.usecase.AuthService

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    @SecurityRequirements
    fun login(
        @RequestBody request: LoginRequest
    ): AuthResponse =
        authService.login(request)

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterUserRequest
    ): AuthResponse =
        authService.register(request)
}
