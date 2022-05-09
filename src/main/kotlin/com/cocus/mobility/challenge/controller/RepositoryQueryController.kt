package com.cocus.mobility.challenge.controller

import com.cocus.mobility.challenge.controller.dto.RepositoryControllerMapper
import com.cocus.mobility.challenge.controller.dto.RepositoryDto
import com.cocus.mobility.challenge.service.RepositoryQueryService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@Tag(name = "repository-query")
class RepositoryQueryController(
    @Autowired private val queryService: RepositoryQueryService, @Autowired private val mapper: RepositoryControllerMapper,
) : RepositoryQueryApi {

    override fun getRepositoriesByUsername(@PathVariable username: String): Flux<RepositoryDto> {
        return queryService.getRepositoriesByUsername(username).map(mapper::toRepositoryDto)
    }
}
