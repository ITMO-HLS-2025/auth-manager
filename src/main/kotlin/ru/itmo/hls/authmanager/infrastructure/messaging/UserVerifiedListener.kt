package ru.itmo.hls.authmanager.infrastructure.messaging

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import ru.itmo.hls.authmanager.application.dto.UserVerifiedEvent
import ru.itmo.hls.authmanager.application.usecase.UserVerificationService

@Service
class UserVerifiedListener(
    private val userVerificationService: UserVerificationService
) {

    private val log = LoggerFactory.getLogger(UserVerifiedListener::class.java)

    @RabbitListener(queues = ["auth-manager.user-verified"])
    fun handle(event: UserVerifiedEvent) {
        val updated = userVerificationService.verifyUser(event)
        if (!updated) {
            log.warn("User not found for user.verified event: userId={}", event.userId)
            return
        }
        log.info("User marked as verified: userId={}", event.userId)
    }
}
