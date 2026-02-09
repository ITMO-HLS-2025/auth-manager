package ru.itmo.hls.authmanager.domain.model

import java.time.LocalDateTime

data class User(
    val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val role: UserRole = UserRole.CUSTOMER,
    val theatreId: Long? = null,
    val verified: Boolean = false,
    val verifiedAt: LocalDateTime? = null
)
