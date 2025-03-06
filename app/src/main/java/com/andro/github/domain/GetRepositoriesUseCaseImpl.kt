package com.andro.github.domain

import com.andro.github.data.Repository
import com.andro.github.data.RepositoryRepository
import javax.inject.Inject

class GetRepositoriesUseCaseImpl
    @Inject
    constructor(
        private val repository: RepositoryRepository,
    ) : GetRepositoriesUseCase {
        override suspend fun execute(language: String): Result<List<Repository>> = repository.fetchRepositories(language)
    }
