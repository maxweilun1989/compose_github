package com.andro.github.data

import com.squareup.moshi.Json

data class Owner(
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "login") val login: String,
)
