package ru.itmo.hls.authmanager.application.dto

import java.time.LocalDateTime

data class ApiErrorDto(
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
