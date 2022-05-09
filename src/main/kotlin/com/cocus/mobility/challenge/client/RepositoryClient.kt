package com.cocus.mobility.challenge.client

import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface RepositoryClient {

    fun getRepositoriesByUsername(username: String): Flux<Repository>

    fun getRepositoryBranchesByUsername(repository: Repository, username: String): Mono<List<Branch>>
}