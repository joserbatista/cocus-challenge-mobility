package com.cocus.mobility.challenge.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Repository")
data class RepositoryDto(
    @field:Schema(example = "sample-repo") val name: String,
    @field:Schema(example = "joserbatista") val owner: String? = null,
    val branchList: List<BranchDto>?,
)