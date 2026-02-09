package ru.itmo.hls.authmanager.application.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import ru.itmo.hls.authmanager.AbstractIntegrationTest
import ru.itmo.hls.authmanager.application.dto.LoginRequest
import ru.itmo.hls.authmanager.application.dto.RegisterUserRequest
import ru.itmo.hls.authmanager.domain.exception.InvalidCredentialsException
import ru.itmo.hls.authmanager.domain.exception.UserAlreadyExistsException
import ru.itmo.hls.authmanager.domain.model.UserRole

@SpringBootTest
@Transactional
class AuthServiceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var authService: AuthService

    @Test
    @DisplayName("Регистрация CUSTOMER — успешный кейс")
    fun registerCustomerSuccess() {
        val request = RegisterUserRequest(
            username = "customer1",
            password = "pass123",
            role = UserRole.CUSTOMER,
            theatreId = null
        )
        val response = authService.register(request)

        assertNotNull(response.user.id)
        assertEquals(UserRole.CUSTOMER, response.user.role)
        assertEquals("customer1", response.user.username)
        assertNotNull(response.token)
    }

    @Test
    @DisplayName("Регистрация THEATRE_DIRECTOR без theatreId — ошибка")
    fun registerDirectorWithoutTheatreId() {
        val request = RegisterUserRequest(
            username = "director1",
            password = "pass123",
            role = UserRole.THEATRE_DIRECTOR,
            theatreId = null
        )
        assertThrows<IllegalArgumentException> {
            authService.register(request)
        }
    }

    @Test
    @DisplayName("Регистрация THEATRE_DIRECTOR с theatreId — успешный кейс")
    fun registerDirectorWithTheatreId() {
        val request = RegisterUserRequest(
            username = "director2",
            password = "pass123",
            role = UserRole.THEATRE_DIRECTOR,
            theatreId = 10L
        )
        val response = authService.register(request)

        assertNotNull(response.user.id)
        assertEquals(UserRole.THEATRE_DIRECTOR, response.user.role)
        assertEquals(10L, response.user.theatreId)
    }

    @Test
    @DisplayName("Регистрация ADMIN — ошибка")
    fun registerAdminRejected() {
        val request = RegisterUserRequest(
            username = "admin2",
            password = "pass123",
            role = UserRole.ADMIN,
            theatreId = null
        )
        assertThrows<IllegalArgumentException> {
            authService.register(request)
        }
    }

    @Test
    @DisplayName("Регистрация с существующим username — ошибка")
    fun registerDuplicateUsername() {
        val request = RegisterUserRequest(
            username = "dupuser",
            password = "pass123",
            role = UserRole.CUSTOMER,
            theatreId = null
        )
        authService.register(request)

        assertThrows<UserAlreadyExistsException> {
            authService.register(request)
        }
    }

    @Test
    @DisplayName("Логин — успешный кейс")
    fun loginSuccess() {
        val register = RegisterUserRequest(
            username = "loginuser",
            password = "pass123",
            role = UserRole.CUSTOMER,
            theatreId = null
        )
        authService.register(register)

        val response = authService.login(LoginRequest(username = "loginuser", password = "pass123"))
        assertEquals("loginuser", response.user.username)
        assertNotNull(response.token)
    }

    @Test
    @DisplayName("Логин — ошибка, неверный пароль")
    fun loginInvalidPassword() {
        val register = RegisterUserRequest(
            username = "badpass",
            password = "pass123",
            role = UserRole.CUSTOMER,
            theatreId = null
        )
        authService.register(register)

        assertThrows<InvalidCredentialsException> {
            authService.login(LoginRequest(username = "badpass", password = "wrong"))
        }
    }

    @Test
    @DisplayName("Логин — ошибка, пользователь не найден")
    fun loginUserNotFound() {
        assertThrows<InvalidCredentialsException> {
            authService.login(LoginRequest(username = "missing", password = "pass123"))
        }
    }
}
