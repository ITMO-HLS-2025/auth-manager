package ru.itmo.hls.authmanager.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.hls.authmanager.dto.AuthResponse
import ru.itmo.hls.authmanager.dto.LoginRequest
import ru.itmo.hls.authmanager.dto.RegisterUserRequest
import ru.itmo.hls.authmanager.dto.UserResponse
import ru.itmo.hls.authmanager.entity.User
import ru.itmo.hls.authmanager.entity.UserRole
import ru.itmo.hls.authmanager.exception.InvalidCredentialsException
import ru.itmo.hls.authmanager.exception.UserAlreadyExistsException
import ru.itmo.hls.authmanager.repository.UserRepository

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    @Transactional
    fun register(request: RegisterUserRequest): AuthResponse {
        validateRegistration(request)
        val normalizedUsername = request.username.trim()
        if (userRepository.existsByUsername(normalizedUsername)) {
            throw UserAlreadyExistsException("User with username $normalizedUsername already exists")
        }
        val user = User(
            username = normalizedUsername,
            passwordHash = passwordEncoder.encode(request.password),
            role = request.role,
            theatreId = request.theatreId
        )
        val saved = userRepository.save(user)
        return saved.toAuthResponse()
    }

    fun login(request: LoginRequest): AuthResponse {
        validateLogin(request)
        val user = userRepository.findByUsername(request.username.trim())
            ?: throw InvalidCredentialsException("Invalid username or password")
        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw InvalidCredentialsException("Invalid username or password")
        }
        return user.toAuthResponse()
    }

    private fun validateRegistration(request: RegisterUserRequest) {
        require(request.username.isNotBlank()) { "Username must not be blank" }
        require(request.password.isNotBlank()) { "Password must not be blank" }
        require(request.role != UserRole.ADMIN) { "Admin role cannot be assigned via API" }
        when (request.role) {
            UserRole.THEATRE_DIRECTOR -> requireNotNull(request.theatreId) {
                "Theatre director must have theatreId"
            }
            else -> require(request.theatreId == null) {
                "Theatre id is only allowed for theatre director"
            }
        }
    }

    private fun validateLogin(request: LoginRequest) {
        require(request.username.isNotBlank()) { "Username must not be blank" }
        require(request.password.isNotBlank()) { "Password must not be blank" }
    }

    private fun User.toResponse(): UserResponse =
        UserResponse(
            id = id,
            username = username,
            role = role,
            theatreId = theatreId
        )

    private fun User.toAuthResponse(): AuthResponse =
        AuthResponse(
            token = jwtService.generateToken(this),
            user = toResponse()
        )
}
