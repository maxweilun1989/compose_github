package com.andro.github.app

object AppConfig {
    const val TITLE = "GitHub Top 10 Repositories"

    // for language
    const val DEFAULT_LANGUAGE = "kotlin"
    val LNAGUAGE_LIST =
        listOf(
            "kotlin",
            "java",
            "python",
            "javascript",
            "typescript",
            "c++",
            "c#",
            "php",
            "ruby",
            "swift",
        )

    const val ROUTE_REPOSITORY_LIST = "repository_list"
    const val ROUTER_LOADING = "loading"
    const val ROUTER_ERROR = "error"
    const val ROUTER_REPOSITORY_DETAIL = "repository_detail"
    const val ROUTER_LOGIN = "login"
    const val ROUTER_PROFILE = "profile"

    const val CLIENT_SECRETS = "19e027173588092de2a6b022539a9ee3e94d99ca"
    const val CLIENT_ID = "Iv23liUcbd4F4mVIacoA"
    const val KEY_ACCESS_TOKEN = "github_access_token"

    const val REDIRECT_URI = "andro://githubredirect"
    const val AUTH_URL = "https://github.com/login/oauth/authorize"
}
