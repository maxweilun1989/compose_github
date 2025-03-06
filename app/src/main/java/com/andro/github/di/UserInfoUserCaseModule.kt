package com.andro.github.di

import com.andro.github.data.AccountRepository
import com.andro.github.data.AccountRepositoryImpl
import com.andro.github.domain.GetGitHubUseInfoCase
import com.andro.github.domain.GetGitHubUseInfoCaseImpl
import com.andro.github.network.GitHubApiService
import com.andro.github.network.GitHubAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UserInfoUserCaseModule {
    @Provides
    fun provideGetUserInfoUserCase(repository: AccountRepository): GetGitHubUseInfoCase = GetGitHubUseInfoCaseImpl(repository)

    @Provides
    fun provideAccountRepository(
        gitHubApiService: GitHubApiService,
        githubAuthService: GitHubAuthService,
    ): AccountRepository = AccountRepositoryImpl(githubAuthService, gitHubApiService)
}
