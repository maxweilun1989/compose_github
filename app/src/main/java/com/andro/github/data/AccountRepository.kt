package com.andro.github.data

interface AccountRepository {
    suspend fun getAccessToken(code: String): String

    suspend fun getUserInfo(accessToken: String): GitHubUser

    suspend fun getUserRepos(accessToken: String): List<Repository>

    fun saveAccessToken(accessToken: String)

    fun getSavedAccessToken(): String?

    fun removeSavedAccessToken()
}
