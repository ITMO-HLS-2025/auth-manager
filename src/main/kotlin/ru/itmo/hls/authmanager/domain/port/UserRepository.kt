package ru.itmo.hls.authmanager.domain.port

import ru.itmo.hls.authmanager.domain.model.User

interface UserRepository {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun save(user: User): User
    fun findById(id: Long): User?
}
