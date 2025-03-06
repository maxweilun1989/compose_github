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
}
