package com.cocus.mobility.challenge.controller

import com.cocus.mobility.challenge.controller.dto.ErrorDto
import com.cocus.mobility.challenge.entity.ErrorEntity
import com.cocus.mobility.challenge.entity.exception.ClientBusinessErrorException
import com.cocus.mobility.challenge.entity.exception.ClientInternalErrorException
import com.cocus.mobility.challenge.entity.exception.NotFoundException
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    companion object {

        private val log: Logger = LoggerFactory.getLogger(RestExceptionHandler::class.java)

        private val EXCEPTION_HTTP_STATUS_MAP: Map<Class<out Exception>, HttpStatus> = mapOf(
            NotFoundException::class.java to HttpStatus.NOT_FOUND,
            ClientBusinessErrorException::class.java to HttpStatus.BAD_REQUEST,
            ClientInternalErrorException::class.java to HttpStatus.BAD_GATEWAY
        )
    }

    @ExceptionHandler(Exception::class)
    @ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorDto::class))])
    fun handleException(ex: Exception): ResponseEntity<ErrorEntity> {
        val httpStatus = EXCEPTION_HTTP_STATUS_MAP[ex.javaClass] ?: HttpStatus.INTERNAL_SERVER_ERROR
        log.debug("Wrapping <${ex.javaClass}> with status <${httpStatus}>")
        return ResponseEntity.status(httpStatus).body(ErrorEntity(httpStatus, ex.message))
    }
}