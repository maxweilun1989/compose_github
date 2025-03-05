package com.andro.github.data

import com.andro.github.network.GitHubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryRepository
    @Inject
    constructor(
        private val api: GitHubApiService,
    ) {
        suspend fun fetchRepositories(language: String): Result<List<Repository>> =
            withContext(Dispatchers.IO) {
                try {
                    val repos = api.getPopularRepositories(query = language).items
                    Result.success(repos)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
    }
