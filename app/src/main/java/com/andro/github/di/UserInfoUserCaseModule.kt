package com.andro.github.di

import android.content.Context
import android.content.SharedPreferences
import com.andro.github.data.AccountRepository
import com.andro.github.data.AccountRepositoryImpl
import com.andro.github.domain.GetGitHubUseInfoCase
import com.andro.github.domain.GetGitHubUseInfoCaseImpl
import com.andro.github.network.GitHubApiService
import com.andro.github.network.GitHubAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserInfoUserCaseModule {
    @Provides
    fun provideGetUserInfoUserCase(repository: AccountRepository): GetGitHubUseInfoCase = GetGitHubUseInfoCaseImpl(repository)

    @Provides
    fun provideAccountRepository(
        gitHubApiService: GitHubApiService,
        githubAuthService: GitHubAuthService,
        sharedPreferences: SharedPreferences,
    ): AccountRepository = AccountRepositoryImpl(githubAuthService, gitHubApiService, sharedPreferences)

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
}
