package ru.uomkri.skbtest.repo.net

import com.squareup.moshi.Json

data class Repo(
    val id: Int,
    val name: String,
    @Json(name = "full_name")
    val fullName: String,
    val owner: User,
    val description: String?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "forks_count")
    val forksCount: Int?,
    @Json(name = "stargazers_count")
    val stargazersCount: Int?

)

data class User(
    val id: Int,
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String?
)

data class SearchResponse(
    @Json(name = "total_count")
    val totalCount: Int,
    @Json(name = "incomplete_results")
    val incompleteResults: Boolean,
    val items: List<Repo>
)