package com.andro.github.di

import com.andro.github.data.Owner
import com.andro.github.data.Repository
import com.andro.github.domain.GetRepositoriesUseCase

class FakeGetReposErrorUserCase : GetRepositoriesUseCase {
    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun execute(language: String): Result<List<Repository>> {
        if (shouldReturnError) {
            return Result.failure(Exception("Test error"))
        }
        val mockRepositories =
            List(10) { index ->
                Repository(
                    id = index,
                    name = "name$index",
                    fullName = "full_name$index",
                    owner = Owner("Owner$index", "https://example.com/avatar$index.png"),
                    description = "Description for TestRepo$index",
                    stars = index * 10,
                    forks = index * 5,
                    openIssues = index * 2,
                    language = "Kotlin",
                    topics = listOf("topic1", "topic2"),
                )
            }
        return Result.success(mockRepositories)
    }
}
