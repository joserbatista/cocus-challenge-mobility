package com.cocus.mobility.challenge

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "GitHub API", version = "1.0"))
class ChallengeApplication

fun main(args: Array<String>) {
    runApplication<ChallengeApplication>(*args)
}
