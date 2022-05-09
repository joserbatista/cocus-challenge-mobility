package com.cocus.mobility.challenge.client.webclient

import com.cocus.mobility.challenge.entity.exception.ClientBusinessErrorException
import com.cocus.mobility.challenge.entity.exception.ClientInternalErrorException
import com.cocus.mobility.challenge.entity.exception.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Configuration
class WebClientConfig {

    companion object {

        private val log: Logger = LoggerFactory.getLogger(WebClientConfig::class.java)
    }

    @Bean("githubWebClient")
    fun githubWebClient(
        @Value("\${client.github.url:https://api.github.com}") baseUrl: String,
        @Value("\${client.github.username:}") username: String,
        @Value("\${client.github.token:}") token: String,
    ): WebClient {
        log.debug("Returning custom WebClient with url: <$baseUrl>")
        var builder = WebClient.builder().baseUrl(baseUrl).filter(errorHandlerFilter())
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
        if (username.isNotBlank()) { // if username is blank, use no authentication
            builder = builder.defaultHeaders { it.setBasicAuth(username, token) }
        }
        return builder.build()
    }

    fun errorHandlerFilter(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor(::handleResponseStatusCode)
    }

    private fun handleResponseStatusCode(clientResponse: ClientResponse): Mono<ClientResponse> {
        val statusCode = clientResponse.statusCode()
        if (statusCode.is2xxSuccessful) {
            return Mono.just(clientResponse)
        }

        val errorMono = clientResponse.bodyToMono(ErrorDto::class.java)

        val exceptionMono = when {
            statusCode == HttpStatus.NOT_FOUND -> errorMono.map { NotFoundException(it.message) }
            statusCode.is4xxClientError -> errorMono.map { ClientBusinessErrorException(it.message) }
            else -> errorMono.map { ClientInternalErrorException(it.message) }
        }

        return exceptionMono.flatMap { Mono.error(it) }
    }

}