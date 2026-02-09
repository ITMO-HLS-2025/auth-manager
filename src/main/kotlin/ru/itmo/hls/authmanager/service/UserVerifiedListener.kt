package ru.itmo.hls.authmanager.service

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.hls.authmanager.dto.UserVerifiedEvent
import ru.itmo.hls.authmanager.repository.UserRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class UserVerifiedListener(
    private val userRepository: UserRepository
) {

    private val log = LoggerFactory.getLogger(UserVerifiedListener::class.java)

    @Transactional
    @RabbitListener(queues = ["auth-manager.user-verified"])
    fun handle(event: UserVerifiedEvent) {
        val user = userRepository.findById(event.userId).orElse(null)
        if (user == null) {
            log.warn("User not found for user.verified event: userId={}", event.userId)
            return
        }
        if (user.verified) {
            return
        }
        val verifiedAt = runCatching {
            LocalDateTime.ofInstant(Instant.parse(event.verifiedAt), ZoneOffset.UTC)
        }.getOrElse { LocalDateTime.now() }
        user.verified = true
        user.verifiedAt = verifiedAt
        userRepository.save(user)
        log.info("User marked as verified: userId={}", user.id)
    }
}
