package com.andro.github.domain

import com.andro.github.data.Repository
import com.andro.github.data.RepositoryRepository
import javax.inject.Inject

class GetRepositoriesUseCase
    @Inject
    constructor(
        private val repository: RepositoryRepository,
    ) {
        suspend operator fun invoke(language: String): Result<List<Repository>> = repository.fetchRepositories(language)
    }
