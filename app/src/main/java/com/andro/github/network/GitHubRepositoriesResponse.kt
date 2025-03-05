package com.andro.github.network

import com.andro.github.data.Repository
import com.squareup.moshi.Json

data class GitHubRepositoriesResponse(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "items") val items: List<Repository>,
)
