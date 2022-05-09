package com.cocus.mobility.challenge.client.webclient

import com.fasterxml.jackson.annotation.JsonProperty

data class RepositoryDto(val id: Int, val name: String, val owner: OwnerDto? = null, @field:JsonProperty("fork") val isFork: Boolean = false)

data class OwnerDto(val login: String, val id: Int)

data class BranchDto(val name: String, val commit: CommitDto? = null)

data class CommitDto(val sha: String)

data class ErrorDto(val message: String)