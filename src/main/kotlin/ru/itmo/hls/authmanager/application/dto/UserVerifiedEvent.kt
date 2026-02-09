package ru.itmo.hls.authmanager.application.dto

data class UserVerifiedEvent(
    val userId: Long,
    val verifiedAt: String
)
