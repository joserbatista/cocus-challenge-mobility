package com.cocus.mobility.challenge.client.webclient

import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RepositoryWebClientMapperTest {

    var mapper: RepositoryWebClientMapper = RepositoryWebClientMapper()

    @Test
    fun toRepository_FromRepositoryDto_IsProperlyMapped() {
        val repositoryDto = RepositoryDto(name = "test1", owner = OwnerDto("joserbatista", 36679667), id = 73233303)

        val actual = mapper.toRepository(repositoryDto)
        val expected = Repository(name = "test1", owner = "joserbatista")

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun toBranch_FromBranchDto_IsProperlyMapped() {
        val branchDto = BranchDto(name = "main", CommitDto(sha = "f0e51b63f7b64da4b9050ac7143b5f69"))

        val actual = mapper.toBranch(branchDto)
        val expected = Branch(name = "main", commitHash = "f0e51b63f7b64da4b9050ac7143b5f69")

        Assertions.assertEquals(expected, actual)
    }
}