package com.cocus.mobility.challenge.controller

import com.cocus.mobility.challenge.controller.dto.RepositoryDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Flux

@RequestMapping(path = ["user/{username}/repository"], produces = [MediaType.APPLICATION_JSON_VALUE])
interface RepositoryQueryApi {

    @GetMapping
    @Operation(description = "List all non-fork GitHub repositories for a user, with branches")
    fun getRepositoriesByUsername(@PathVariable username: String): Flux<RepositoryDto>

}