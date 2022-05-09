package com.cocus.mobility.challenge.service

import com.cocus.mobility.challenge.client.RepositoryClient
import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class RepositoryQueryServiceAdapterTest {

    @Mock
    lateinit var client: RepositoryClient

    @InjectMocks
    lateinit var service: RepositoryQueryServiceAdapter

    @Test
    fun getRepositoriesByUsername_ValidResponse_ReturnsRepositoryFlux() {
        val repositoryList = buildRepositoryList()
        whenever(client.getRepositoriesByUsername("joserbatista")).thenReturn(Flux.fromIterable(repositoryList))
        whenever(client.getRepositoryBranchesByUsername(any(), eq("joserbatista"))).thenReturn(Mono.just(buildBranchList()))

        val repositoriesByUsername = service.getRepositoriesByUsername("joserbatista")
        val expectedRepositoryList = buildExpectedRepositoryList()

        StepVerifier.create(repositoriesByUsername).expectNextSequence(expectedRepositoryList).verifyComplete()

        verify(client).getRepositoriesByUsername("joserbatista")
        verify(client, times(2)).getRepositoryBranchesByUsername(any(), eq("joserbatista"))
        verifyNoMoreInteractions(client)
    }

    @Test
    fun getRepositoryBranchesByUsername_ValidResponse_ReturnsBranchFlux() {
        val branchList = buildBranchList()
        whenever(client.getRepositoryBranchesByUsername(any(), eq("joserbatista"))).thenReturn(Mono.just(branchList))
        val repository = Repository("test1", "joserbatista")
        val repositoriesByUsername = service.getRepositoryBranchesByUsername(repository, "joserbatista")

        StepVerifier.create(repositoriesByUsername).expectNext(branchList).verifyComplete()

        verify(client).getRepositoryBranchesByUsername(any(), eq("joserbatista"))
        verifyNoMoreInteractions(client)
    }

    private fun buildBranchList(): List<Branch> = listOf(
        Branch("main", "f0e51b63f7b64da4b9050ac7143b5f69"), Branch("dev", "6c72d6da7564463a16caccb32cf28d5f")
    )

    private fun buildRepositoryList() = listOf(
        Repository("test1", "joserbatista"), Repository("test2", "joserbatista")
    )

    private fun buildExpectedRepositoryList() = listOf(
        Repository("test1", "joserbatista", buildBranchList().toMutableList()),
        Repository("test2", "joserbatista", buildBranchList().toMutableList())
    )
}