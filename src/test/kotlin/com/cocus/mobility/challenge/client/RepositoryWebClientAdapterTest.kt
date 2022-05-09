package com.cocus.mobility.challenge.client

import com.cocus.mobility.challenge.client.webclient.RepositoryWebClientMapper
import com.cocus.mobility.challenge.client.webclient.WebClientConfig
import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import com.cocus.mobility.challenge.entity.exception.ClientBusinessErrorException
import com.cocus.mobility.challenge.entity.exception.ClientInternalErrorException
import com.cocus.mobility.challenge.entity.exception.NotFoundException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import reactor.test.StepVerifier

class RepositoryWebClientAdapterTest {

    private lateinit var client: RepositoryWebClientAdapter
    private lateinit var server: MockWebServer

    private lateinit var mapper: RepositoryWebClientMapper

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        mapper = Mockito.mock(RepositoryWebClientMapper::class.java)

        val baseUrl = server.url("http://localhost:${server.port}").toUrl().toString()
        client = RepositoryWebClientAdapter(
            WebClientConfig().githubWebClient(baseUrl, "talithak0", "d486c8763b3f40949f1d5398fa56da46"),
            mapper
        )
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getRepositoriesByUsername_SuccessResponseCode_HasValidRequestAndSteps() {
        server.enqueue(
            MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""[{"id":457402548,"name":"automata","fork":false},{"id":254845740,"name":"fork","fork":true}]""")
        )

        val repository = Repository("automata")
        whenever(mapper.toRepository(any())).thenReturn(repository)

        StepVerifier.create(client.getRepositoriesByUsername("joserbatista"))
            .expectNextSequence(listOf(repository))
            .verifyComplete()

        val request = server.takeRequest()

        assertEquals("GET", request.method)
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isNotBlank
        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo("/users/joserbatista/repos?per_page=100")
    }

    @Test
    fun getRepositoriesByUsername_NotFoundResponseCode_HasValidRequestAndSteps() {
        server.enqueue(
            MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""{"message":"Not Found","documentation_url":"https://docs.github.com/rest"}""")
        )

        StepVerifier.create(client.getRepositoriesByUsername("joserbatista"))
            .expectError(NotFoundException::class.java)
            .verify()
    }

    @Test
    fun getRepositoriesByUsername_400ResponseCode_HasValidRequestAndSteps() {
        server.enqueue(
            MockResponse().setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""{"message":"Bad Request","documentation_url":"https://docs.github.com/rest"}""")
        )

        StepVerifier.create(client.getRepositoriesByUsername("joserbatista"))
            .expectError(ClientBusinessErrorException::class.java)
            .verify()
    }

    @Test
    fun getRepositoriesByUsername_500ResponseCode_HasValidRequestAndSteps() {
        server.enqueue(
            MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""{"message":"Internal Server Error","documentation_url":"https://docs.github.com/rest"}""")
        )

        StepVerifier.create(client.getRepositoriesByUsername("joserbatista"))
            .expectError(ClientInternalErrorException::class.java)
            .verify()
    }

    @Test
    fun getRepositoryBranchesByUsername_SuccessResponseCode_HasValidRequestAndSteps() {
        server.enqueue(
            MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""[{"name":"master","commit":{"sha":"f0f030bd21910348b92ffc123e07d81e15eabc7c"}}]""")
        )

        val branch = Branch("automata", "f0f030bd21910348b92ffc123e07d81e15eabc7c")
        whenever(mapper.toBranch(any())).thenReturn(branch)

        StepVerifier.create(client.getRepositoryBranchesByUsername(Repository("automata"), "joserbatista"))
            .expectNext(listOf(branch))
            .verifyComplete()

        val request = server.takeRequest()

        assertEquals("GET", request.method)
        assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isNotBlank
        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo("/repos/joserbatista/automata/branches")
    }
}