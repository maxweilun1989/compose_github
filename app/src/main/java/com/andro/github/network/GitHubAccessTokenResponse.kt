package com.andro.github.network

import com.squareup.moshi.Json

data class GitHubAccessTokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "scope") val scope: String,
)
