package ru.itmo.hls.authmanager.application.port

interface PasswordHasher {
    fun encode(raw: String): String
    fun matches(raw: String, encoded: String): Boolean
}
