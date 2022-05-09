package com.cocus.mobility.challenge.client

import com.cocus.mobility.challenge.client.webclient.BranchDto
import com.cocus.mobility.challenge.client.webclient.RepositoryDto
import com.cocus.mobility.challenge.client.webclient.RepositoryWebClientMapper
import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RepositoryWebClientAdapter(
    @Autowired private val webClient: WebClient, @Autowired private val mapper: RepositoryWebClientMapper,
) : RepositoryClient {

    companion object {

        private val log: Logger = LoggerFactory.getLogger(RepositoryWebClientAdapter::class.java)
    }

    override fun getRepositoriesByUsername(username: String): Flux<Repository> {
        log.info("Fetching Repositories for <$username>")
        return webClient.get()
            .uri { it.path("users/{username}/repos").queryParam("per_page", "100").build(username) }.retrieve()
            .bodyToFlux(RepositoryDto::class.java).filter { !it.isFork }.map(mapper::toRepository)
    }

    override fun getRepositoryBranchesByUsername(repository: Repository, username: String): Mono<List<Branch>> {
        val name = repository.name
        log.info("Fetching Branches for <$username>'s repository <$name>")
        return webClient.get().uri("repos/{username}/{repo}/branches", username, repository.name).retrieve()
            .bodyToFlux(BranchDto::class.java).map(mapper::toBranch).collectList()
    }
}