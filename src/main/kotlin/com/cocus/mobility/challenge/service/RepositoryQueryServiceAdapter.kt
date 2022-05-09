package com.cocus.mobility.challenge.service

import com.cocus.mobility.challenge.client.RepositoryClient
import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RepositoryQueryServiceAdapter(@Autowired private val client: RepositoryClient) : RepositoryQueryService {

    companion object {

        private val log: Logger = LoggerFactory.getLogger(RepositoryQueryServiceAdapter::class.java)
    }

    override fun getRepositoriesByUsername(username: String): Flux<Repository> {
        log.info("Fetching Repositories for <$username>")
        return this.client.getRepositoriesByUsername(username).flatMap { repository -> this.fillBranchDetails(repository, username) }
    }

    private fun fillBranchDetails(repository: Repository, username: String): Mono<Repository> {
        log.info("Filling Branches for <$username>'s repository <${repository.name}>")
        val branchListMono = this.getRepositoryBranchesByUsername(repository, username)
        return branchListMono.map { branchList -> this.addBranchListAndReturn(repository, branchList) }
    }

    private fun addBranchListAndReturn(repository: Repository, branch: List<Branch>): Repository {
        log.debug("Adding <${branch.size}> Branches to <${repository.name}>")
        repository.branchList += branch
        return repository
    }

    fun getRepositoryBranchesByUsername(repository: Repository, username: String): Mono<List<Branch>> {
        log.info("Fetching Branches for <$username>'s repository <${repository.name}>")
        return this.client.getRepositoryBranchesByUsername(repository, username)
    }

}