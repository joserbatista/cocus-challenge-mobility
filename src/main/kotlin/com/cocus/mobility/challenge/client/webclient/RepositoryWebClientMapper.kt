package com.cocus.mobility.challenge.client.webclient

import com.cocus.mobility.challenge.entity.Branch
import com.cocus.mobility.challenge.entity.Repository
import org.springframework.stereotype.Service

@Service
class RepositoryWebClientMapper {

    fun toRepository(repositoryDto: RepositoryDto): Repository = Repository(repositoryDto.name, repositoryDto.owner?.login)

    fun toBranch(branchDto: BranchDto): Branch = Branch(branchDto.name, branchDto.commit?.sha)
}