package com.cocus.mobility.challenge.entity.exception

data class ClientInternalErrorException(val errorBody: String) : Exception(errorBody)
