package ru.itmo.hls.authmanager

import org.springframework.test.context.TestPropertySource
import ru.itmo.hls.authmanager.service.PostgresContainerConfig

@TestPropertySource(
    properties = [
        "auth.jwt.secret=testsecrettestsecrettestsecrettestsecret",
        "auth.jwt.ttl-seconds=3600"
    ]
)
abstract class AbstractIntegrationTest : PostgresContainerConfig()
