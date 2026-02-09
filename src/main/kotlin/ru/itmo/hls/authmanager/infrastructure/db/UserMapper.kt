package ru.itmo.hls.authmanager.infrastructure.db

import ru.itmo.hls.authmanager.domain.model.User

fun UserEntity.toDomain(): User =
    User(
        id = id,
        username = username,
        passwordHash = passwordHash,
        role = role,
        theatreId = theatreId,
        verified = verified,
        verifiedAt = verifiedAt
    )

fun User.toEntity(): UserEntity =
    UserEntity(
        id = id,
        username = username,
        passwordHash = passwordHash,
        role = role,
        theatreId = theatreId,
        verified = verified,
        verifiedAt = verifiedAt
    )
