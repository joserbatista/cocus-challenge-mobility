package com.cocus.mobility.challenge.entity.exception

data class ClientBusinessErrorException(val errorBody: String) : Exception(errorBody)
