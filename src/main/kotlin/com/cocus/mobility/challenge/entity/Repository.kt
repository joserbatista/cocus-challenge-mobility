package com.cocus.mobility.challenge.entity

data class Repository(val name: String, val owner: String? = null, val branchList: MutableList<Branch> = mutableListOf())