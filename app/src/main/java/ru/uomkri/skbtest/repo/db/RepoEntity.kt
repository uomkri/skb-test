package ru.uomkri.skbtest.repo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.uomkri.skbtest.repo.net.Repo
import ru.uomkri.skbtest.repo.net.User

@Entity(tableName = "favRepos", primaryKeys = ["id", "uid"])
data class RepoEntity(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "owner_id") val ownerId: Int,
    @ColumnInfo(name = "owner_login") val ownerLogin: String,
    @ColumnInfo(name = "owner_avatar") val ownerAvatar: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "created_at") val createdAt: String?,
    @ColumnInfo(name = "forks_count") val forksCount: Int?,
    @ColumnInfo(name = "stargazers_count") val stargazersCount: Int?
)

