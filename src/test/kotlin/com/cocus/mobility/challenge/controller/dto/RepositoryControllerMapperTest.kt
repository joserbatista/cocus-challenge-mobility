package com.cocus.mobility.challenge.controller.dto

import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class RepositoryControllerMapperTest {

    var mapper = RepositoryControllerMapper()

    @Test
    fun toRepositoryDto_FromRepository_IsProperlyMapped() {
        val repository = Repository(name = "test1", owner = "joserbatista",
                                    branchList = mutableListOf(Branch(name = "main", commitHash = "f0e51b63f7b64da4b9050ac7143b5f69")))

        val actual = mapper.toRepositoryDto(repository)
        val expected = RepositoryDto(name = "test1", owner = "joserbatista",
                                     branchList = mutableListOf(BranchDto(name = "main", commitHash = "f0e51b63f7b64da4b9050ac7143b5f69")))

        Assertions.assertEquals(expected, actual)
    }
}