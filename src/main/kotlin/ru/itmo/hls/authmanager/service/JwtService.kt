package ru.itmo.hls.authmanager.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import ru.itmo.hls.authmanager.config.JwtProperties
import ru.itmo.hls.authmanager.entity.User
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {

    fun generateToken(user: User): String {
        val secretBytes = jwtProperties.secret.toByteArray(StandardCharsets.UTF_8)
        val key = Keys.hmacShaKeyFor(secretBytes)
        val now = Instant.now()
        val expiresAt = now.plusSeconds(jwtProperties.ttlSeconds)
        return Jwts.builder()
            .subject(user.username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .claim("userId", user.id)
            .claim("role", user.role.name)
            .claim("theatreId", user.theatreId)
            .signWith(key)
            .compact()
    }
}
