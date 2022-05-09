package com.cocus.mobility.challenge.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Error")
data class ErrorDto(
    @field:Schema(example = "BAD_REQUEST") val statusCode: String,
    @field:Schema(example = "API rate limit exceeded") val message: String? = "Unknown error",
)