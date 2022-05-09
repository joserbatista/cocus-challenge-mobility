package com.cocus.mobility.challenge.controller

import com.cocus.mobility.challenge.controller.dto.RepositoryControllerMapper
import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.ErrorEntity
import com.cocus.mobility.challenge.entity.Repository
import com.cocus.mobility.challenge.entity.exception.NotFoundException
import com.cocus.mobility.challenge.service.RepositoryQueryService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@WebFluxTest(controllers = [RepositoryQueryController::class])
@Import(RestExceptionHandler::class, GlobalErrorWebExceptionHandler::class, RepositoryControllerMapper::class)
class RepositoryQueryControllerTest {

    @MockBean
    lateinit var repositoryQueryService: RepositoryQueryService

    @Autowired
    lateinit var controllerMapper: RepositoryControllerMapper

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun getRepositoriesByUsername_ValidResponse_ReturnsOk() {
        whenever(repositoryQueryService.getRepositoriesByUsername("joserbatista"))
            .thenReturn(Flux.fromIterable(buildRepositoryList()))

        webClient.get().uri("/user/joserbatista/repository").header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Repository::class.java).hasSize(2)

        verify(repositoryQueryService).getRepositoriesByUsername("joserbatista")
        verifyNoMoreInteractions(repositoryQueryService)
    }

    @Test
    fun getRepositoriesByUsername_NotFoundExceptionThrown_Returns404WithBody() {
        whenever(repositoryQueryService.getRepositoriesByUsername("joserbatista"))
            .thenThrow(NotFoundException("Repository Not Found"))

        webClient.get().uri("/user/joserbatista/repository").header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isNotFound
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(ErrorEntity::class.java)
            .isEqualTo(ErrorEntity(HttpStatus.NOT_FOUND, "Repository Not Found"))

        verify(repositoryQueryService).getRepositoriesByUsername("joserbatista")
        verifyNoMoreInteractions(repositoryQueryService)
    }

    @Test
    fun getRepositoriesByUsername_UnsupportedAcceptHeader_Returns406WithBody() {
        webClient.get().uri("/user/joserbatista/repository").header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
            .expectBody(ErrorEntity::class.java)
            .isEqualTo(ErrorEntity(HttpStatus.NOT_ACCEPTABLE, "Could not find acceptable representation"))

        verifyNoInteractions(repositoryQueryService)
    }

    private fun buildRepositoryList() = listOf(
        Repository(name = "test1", owner = "joserbatista", branchList = mutableListOf(
            Branch(name = "main", commitHash = "f0e51b63f7b64da4b9050ac7143b5f69"),
            Branch(name = "dev", commitHash = "6c72d6da7564463a16caccb32cf28d5f")
        )),
        Repository(name = "test2", owner = "joserbatista", branchList = mutableListOf(
            Branch(name = "main", commitHash = "b299a616ea3e4a98aab8c886f5228060"),
            Branch(name = "sit", commitHash = "18bda71122c043dfab0ee132a9f290b2")
        ))
    )
}