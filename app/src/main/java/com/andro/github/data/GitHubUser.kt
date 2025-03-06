package com.andro.github.data

import com.squareup.moshi.Json

data class GitHubUser(
    @Json(name = "id") val id: Long,
    @Json(name = "login") val login: String,
    @Json(name = "name") val name: String?,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "email") val email: String?,
    @Json(name = "bio") val bio: String?,
    @Json(name = "location") val location: String?,
)
