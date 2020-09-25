package ru.uomkri.skbtest.utils

import ru.uomkri.skbtest.repo.db.RepoEntity
import ru.uomkri.skbtest.repo.net.Repo
import ru.uomkri.skbtest.repo.net.User

fun List<RepoEntity>.asDomainModel(): List<Repo> {
    return map {
        Repo(
            id = it.id,
            name = it.name,
            fullName = it.fullName,
            owner = User(
                login = it.ownerLogin,
                avatarUrl = it.ownerAvatar,
                id = it.ownerId
            ),
            description = it.description,
            createdAt = it.createdAt,
            stargazersCount = it.stargazersCount,
            forksCount = it.forksCount
        )
    }
}

fun asDatabaseModel(repo: Repo, uid: String): RepoEntity {
    return RepoEntity(
        id = repo.id,
        name = repo.name,
        fullName = repo.fullName,
        ownerId = repo.owner.id,
        ownerLogin = repo.owner.login,
        ownerAvatar = repo.owner.avatarUrl,
        description = repo.description,
        createdAt = repo.createdAt,
        forksCount = repo.forksCount,
        stargazersCount = repo.stargazersCount,
        uid = uid
    )
}
