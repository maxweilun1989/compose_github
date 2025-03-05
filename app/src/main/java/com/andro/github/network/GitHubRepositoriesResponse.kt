package com.andro.github.network

import com.squareup.moshi.Json

data class GitHubRepositoriesResponse(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "items") val items: List<Repository>,
)

data class Repository(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "html_url") val htmlUrl: String,
    @Json(name = "stargazers_count") val stars: Int,
    @Json(name = "language") val language: String?,
    @Json(name = "description") val description: String?,
)
