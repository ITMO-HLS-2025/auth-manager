package ru.itmo.hls.authmanager.dto

data class UserVerifiedEvent(
    val userId: Long,
    val verifiedAt: String
)
