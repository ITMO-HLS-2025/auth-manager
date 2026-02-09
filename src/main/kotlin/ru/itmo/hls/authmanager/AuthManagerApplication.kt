package ru.itmo.hls.authmanager

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableRabbit
class AuthManagerApplication

fun main(args: Array<String>) {
    runApplication<AuthManagerApplication>(*args)
}
