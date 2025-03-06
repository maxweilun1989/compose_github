package com.andro.github.domain

import android.util.Log
import com.andro.github.data.AccountRepository
import com.andro.github.data.GitHubUser
import javax.inject.Inject

class GetGitHubUseInfoCaseImpl
    @Inject
    constructor(
        private val accountRepository: AccountRepository,
    ) : GetGitHubUseInfoCase {
        companion object {
            const val TAG = "[GetGitHubUseInfoCaseImpl]"
        }

        override suspend fun execute(code: String): Result<GitHubUser> =
            kotlin
                .runCatching {
                    val accessToken = accountRepository.getAccessToken(code)
                    if (accessToken.isEmpty()) {
                        throw IllegalStateException("Access token isempty")
                    }
                    accountRepository.saveAccessToken(accessToken)
                    val gitHubUser = accountRepository.getUserInfo(accessToken)
                    return@runCatching gitHubUser
                }.onFailure {
                    Log.e(TAG, "Error: ${it.message}")
                    accountRepository.removeSavedAccessToken()
                }
    }
