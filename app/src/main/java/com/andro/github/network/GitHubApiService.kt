package com.andro.github.network

import com.andro.github.data.GitHubUser
import com.andro.github.data.Repository
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("user/repos")
    suspend fun fetchOwnRepos(
        @Header("Authorization") authorization: String,
    ): List<Repository>

    @POST("repos/{owner}/{repo}/issues")
    suspend fun createIssue(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String = "application/vnd.github+json",
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body issueRequest: CreateIssueRequest,
    ): IssueResponse
}

data class CreateIssueRequest(
    val title: String,
    val body: String? = null,
    val assignees: List<String>? = null,
    val labels: List<String>? = null,
)

data class IssueResponse(
    val id: Int,
    val title: String,
    val body: String?,
    val state: String,
    val htmlUrl: String,
)
