package ru.itmo.hls.authmanager.application.port

import ru.itmo.hls.authmanager.domain.model.User

interface TokenProvider {
    fun generateToken(user: User): String
}
