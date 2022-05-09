package com.cocus.mobility.challenge.entity.exception

data class NotFoundException(val errorBody: String) : RuntimeException(errorBody)
