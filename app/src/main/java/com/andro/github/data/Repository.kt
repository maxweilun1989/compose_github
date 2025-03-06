package com.andro.github.data

import com.squareup.moshi.Json

data class Repository(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "owner") val owner: Owner,
    @Json(name = "description") val description: String?,
    @Json(name = "stargazers_count") val stars: Int?,
    @Json(name = "forks_count") val forks: Int?,
    @Json(name = "open_issues_count") val openIssues: Int?,
    @Json(name = "topics") val topics: List<String>,
    @Json(name = "language") val language: String?,
)
