package com.andro.github.common

import com.andro.github.data.AccountRepository
import com.andro.github.data.GitHubUser
import com.andro.github.data.Repository

class FakeAccountRepo : AccountRepository {
    override suspend fun getAccessToken(code: String): String = ""

    override suspend fun getUserInfo(accessToken: String): GitHubUser =
        GitHubUser(
            id = 1L,
            login = "mock_login",
            name = "Mock User",
            avatarUrl = "https://example.com/avatar.png",
            bio = "This is a mock user",
            email = "xxx@gmail.com",
            location = "Wuhan",
        )

    override suspend fun getUserRepos(accessToken: String): List<Repository> = emptyList()

    override fun saveAccessToken(accessToken: String) {
    }

    override fun getSavedAccessToken(): String? = null

    override fun removeSavedAccessToken() {
    }
}
