package com.andro.github.data

import android.content.SharedPreferences
import com.andro.github.app.AppConfig
import com.andro.github.network.CreateIssueRequest
import com.andro.github.network.GitHubApiService
import com.andro.github.network.GitHubAuthService
import com.andro.github.network.IssueResponse
import javax.inject.Inject

class AccountRepositoryImpl
    @Inject
    constructor(
        private val githubAuthService: GitHubAuthService,
        private val githubAPiService: GitHubApiService,
        private val sharedPreferences: SharedPreferences,
    ) : AccountRepository {
        override suspend fun getAccessToken(code: String): String {
            val response =
                githubAuthService.getAccessToken(
                    clientId = AppConfig.CLIENT_ID,
                    clientSecret = AppConfig.CLIENT_SECRETS,
                    code = code,
                )
            return response.accessToken
        }

        override suspend fun getUserInfo(accessToken: String): GitHubUser = githubAPiService.getUserInfo("Bearer $accessToken")

        override suspend fun getUserRepos(accessToken: String): List<Repository> = githubAPiService.fetchOwnRepos("Bearer $accessToken")

        override suspend fun createIssue(
            accessToken: String,
            owner: String,
            repo: String,
            title: String,
            body: String?,
        ): IssueResponse =
            githubAPiService.createIssue(
                token = "Bearer $accessToken",
                owner = owner,
                repo = repo,
                issueRequest = CreateIssueRequest(title, body),
            )

        override fun saveAccessToken(accessToken: String) {
            sharedPreferences
                .edit()
                .putString(AppConfig.KEY_ACCESS_TOKEN, accessToken)
                .apply()
        }

        override fun getSavedAccessToken(): String? = sharedPreferences.getString(AppConfig.KEY_ACCESS_TOKEN, null)

        override fun removeSavedAccessToken() {
            sharedPreferences
                .edit()
                .remove(AppConfig.KEY_ACCESS_TOKEN)
                .apply()
        }
    }
