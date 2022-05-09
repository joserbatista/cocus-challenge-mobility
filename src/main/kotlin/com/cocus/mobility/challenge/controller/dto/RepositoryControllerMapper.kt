package com.cocus.mobility.challenge.controller.dto

import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import org.springframework.stereotype.Component

@Component
class RepositoryControllerMapper {

    fun toRepositoryDto(repository: Repository): RepositoryDto {
        val branchList = this.toBranchDtoList(repository.branchList)
        return RepositoryDto(repository.name, repository.owner, branchList)
    }

    private fun toBranchDtoList(branchList: List<Branch>): List<BranchDto> = branchList.map(::toBranchDto)

    private fun toBranchDto(branch: Branch) = BranchDto(branch.name, branch.commitHash)
}