package ru.itmo.hls.authmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthManagerApplication

fun main(args: Array<String>) {
    runApplication<AuthManagerApplication>(*args)
}
