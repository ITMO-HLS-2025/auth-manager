package ru.itmo.hls.authmanager.infrastructure.db

import org.springframework.stereotype.Repository
import ru.itmo.hls.authmanager.domain.model.User
import ru.itmo.hls.authmanager.domain.port.UserRepository

@Repository
class UserRepositoryAdapter(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun findByUsername(username: String): User? =
        userJpaRepository.findByUsername(username)?.toDomain()

    override fun existsByUsername(username: String): Boolean =
        userJpaRepository.existsByUsername(username)

    override fun save(user: User): User =
        userJpaRepository.save(user.toEntity()).toDomain()

    override fun findById(id: Long): User? =
        userJpaRepository.findById(id).orElse(null)?.toDomain()
}
