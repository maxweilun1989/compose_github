package com.andro.github.network

import com.andro.github.data.GitHubUser
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val REPO_PER_PAGE = 10

interface GitHubApiService {
    @GET("search/repositories")
    suspend fun getPopularRepositories(
        @Query("q") query: String = "stars:>1",
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = REPO_PER_PAGE,
    ): GitHubRepositoriesResponse

    @GET("user")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String,
    ): GitHubUser
}
