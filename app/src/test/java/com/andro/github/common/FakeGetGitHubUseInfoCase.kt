package com.andro.github.common

import com.andro.github.data.GitHubUser
import com.andro.github.domain.GetGitHubUseInfoCase

class FakeGetGitHubUseInfoCase : GetGitHubUseInfoCase {
    override suspend fun execute(code: String): Result<GitHubUser> =
        Result.success(
            GitHubUser(
                id = 1L,
                login = "mock_login",
                name = "Mock User",
                avatarUrl = "https://example.com/avatar.png",
                bio = "This is a mock user",
                email = "xxx@gmail.com",
                location = "wuhan",
            ),
        )
}
