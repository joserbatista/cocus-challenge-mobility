package com.cocus.mobility.challenge.service

import com.cocus.mobility.challenge.entity.Repository
import reactor.core.publisher.Flux

interface RepositoryQueryService {

    fun getRepositoriesByUsername(username: String): Flux<Repository>

}