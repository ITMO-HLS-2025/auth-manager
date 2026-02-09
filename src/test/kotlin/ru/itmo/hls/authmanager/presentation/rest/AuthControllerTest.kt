package ru.itmo.hls.authmanager.presentation.rest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.itmo.hls.authmanager.application.dto.AuthResponse
import ru.itmo.hls.authmanager.application.dto.LoginRequest
import ru.itmo.hls.authmanager.application.dto.RegisterUserRequest
import ru.itmo.hls.authmanager.application.dto.UserResponse
import ru.itmo.hls.authmanager.application.usecase.AuthService
import ru.itmo.hls.authmanager.domain.exception.InvalidCredentialsException
import ru.itmo.hls.authmanager.domain.exception.UserAlreadyExistsException
import ru.itmo.hls.authmanager.domain.model.UserRole

@WebMvcTest(AuthController::class)
@Import(AuthExceptionHandler::class)
class AuthControllerTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var authService: AuthService

    @Test
    fun loginSuccess() {
        val request = LoginRequest(username = "user1", password = "pass123")
        val response = AuthResponse(
            token = "token",
            user = UserResponse(id = 1L, username = "user1", role = UserRole.CUSTOMER, theatreId = null)
        )
        `when`(authService.login(request)).thenReturn(response)

        mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { value("token") }
            jsonPath("$.user.username") { value("user1") }
        }
    }

    @Test
    fun loginInvalidCredentials() {
        val request = LoginRequest(username = "user1", password = "bad")
        `when`(authService.login(request)).thenThrow(InvalidCredentialsException("Invalid username or password"))

        mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.message") { value("Invalid username or password") }
        }
    }

    @Test
    fun registerSuccess() {
        val request = RegisterUserRequest(
            username = "user2",
            password = "pass123",
            role = UserRole.CUSTOMER,
            theatreId = null
        )
        val response = AuthResponse(
            token = "token",
            user = UserResponse(id = 2L, username = "user2", role = UserRole.CUSTOMER, theatreId = null)
        )
        `when`(authService.register(request)).thenReturn(response)

        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.user.username") { value("user2") }
        }
    }

    @Test
    fun registerConflict() {
        val request = RegisterUserRequest(
            username = "user2",
            password = "pass123",
            role = UserRole.CUSTOMER,
            theatreId = null
        )
        `when`(authService.register(request)).thenThrow(UserAlreadyExistsException("User already exists"))

        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isConflict() }
            jsonPath("$.message") { value("User already exists") }
        }
    }

    @Test
    fun registerBadRequest() {
        val request = RegisterUserRequest(
            username = "",
            password = "pass123",
            role = UserRole.CUSTOMER,
            theatreId = null
        )
        `when`(authService.register(request)).thenThrow(IllegalArgumentException("Username must not be blank"))

        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") { value("Username must not be blank") }
        }
    }
}
