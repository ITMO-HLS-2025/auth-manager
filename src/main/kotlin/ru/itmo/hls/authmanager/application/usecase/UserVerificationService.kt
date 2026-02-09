package ru.itmo.hls.authmanager.application.usecase

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.hls.authmanager.application.dto.UserVerifiedEvent
import ru.itmo.hls.authmanager.domain.port.UserRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class UserVerificationService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun verifyUser(event: UserVerifiedEvent): Boolean {
        val user = userRepository.findById(event.userId) ?: return false
        if (user.verified) {
            return true
        }
        val verifiedAt = runCatching {
            LocalDateTime.ofInstant(Instant.parse(event.verifiedAt), ZoneOffset.UTC)
        }.getOrElse { LocalDateTime.now() }
        val updated = user.copy(
            verified = true,
            verifiedAt = verifiedAt
        )
        userRepository.save(updated)
        return true
    }
}
