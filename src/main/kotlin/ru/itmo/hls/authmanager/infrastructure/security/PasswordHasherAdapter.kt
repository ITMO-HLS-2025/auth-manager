package ru.itmo.hls.authmanager.infrastructure.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.itmo.hls.authmanager.application.port.PasswordHasher

@Component
class PasswordHasherAdapter(
    private val passwordEncoder: PasswordEncoder
) : PasswordHasher {

    override fun encode(raw: String): String = passwordEncoder.encode(raw)

    override fun matches(raw: String, encoded: String): Boolean =
        passwordEncoder.matches(raw, encoded)
}
