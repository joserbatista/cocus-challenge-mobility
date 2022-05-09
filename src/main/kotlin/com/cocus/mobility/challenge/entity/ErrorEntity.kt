package com.cocus.mobility.challenge.entity

import org.springframework.http.HttpStatus

data class ErrorEntity(var statusCode: HttpStatus, var message: String? = "Unknown error") {

    fun toMap(): Map<String, Any?> = mapOf("statusCode" to statusCode, "message" to message)
}