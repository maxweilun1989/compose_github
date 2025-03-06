package com.andro.github.data

import com.andro.github.network.IssueResponse

interface AccountRepository {
    suspend fun getAccessToken(code: String): String

    suspend fun getUserInfo(accessToken: String): GitHubUser

    suspend fun getUserRepos(accessToken: String): List<Repository>

    suspend fun createIssue(
        accessToken: String,
        owner: String,
        repo: String,
        title: String,
        body: String? = null,
    ): IssueResponse

    fun saveAccessToken(accessToken: String)

    fun getSavedAccessToken(): String?

    fun removeSavedAccessToken()
}
