package com.andro.github.network

import com.squareup.moshi.Json

data class GitHubRepositoriesResponse(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "items") val items: List<Repository>,
)

data class Repository(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "owner") val owner: Owner,
    @Json(name = "html_url") val htmlUrl: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "stargazers_count") val stars: Int?,
    @Json(name = "forks_count") val forks: Int?,
    @Json(name = "open_issues_count") val openIssues: Int?,
    @Json(name = "topics") val topics: List<String>,
    @Json(name = "language") val language: String?,
)

data class Owner(
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "login") val login: String,
)
