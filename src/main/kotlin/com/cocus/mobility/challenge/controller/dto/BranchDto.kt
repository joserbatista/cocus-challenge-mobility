package com.cocus.mobility.challenge.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Branch")
data class BranchDto(
    @field:Schema(example = "main") val name: String,
    @field:Schema(example = "39e2c2a8b61be79544905727c4c3d0e440412cd8") val commitHash: String?,
)
