package com.andro.github.data

import com.andro.github.app.AppConfig
import com.andro.github.network.GitHubApiService
import com.andro.github.network.GitHubAuthService
import javax.inject.Inject

class AccountRepositoryImpl
    @Inject
    constructor(
        private val githubAuthService: GitHubAuthService,
        private val githubAPiService: GitHubApiService,
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
    }
