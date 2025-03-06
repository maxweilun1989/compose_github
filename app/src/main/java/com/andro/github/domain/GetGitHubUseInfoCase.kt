package com.andro.github.domain

import com.andro.github.data.GitHubUser

interface GetGitHubUseInfoCase {
    suspend fun execute(code: String): Result<GitHubUser>
}
