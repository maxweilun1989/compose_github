package com.andro.github.data

interface AccountRepository {
    suspend fun getAccessToken(code: String): String

    suspend fun getUserInfo(accessToken: String): GitHubUser
}
