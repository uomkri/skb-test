package ru.uomkri.skbtest.repo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface RepoDao {
    @Query("SELECT * FROM favRepos WHERE uid LIKE :uid")
    fun getAllFavRepos(uid: String): Single<List<RepoEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertToFavorites(repo: RepoEntity): Completable

    @Query("DELETE FROM favRepos WHERE id LIKE :repoId AND uid LIKE :uid")
    fun deleteFromFavorites(repoId: Int, uid: String): Completable

    @Query("SELECT * FROM favRepos WHERE id LIKE :repoId AND uid LIKE :uid")
    fun getRepoFromDatabase(repoId: Int, uid: String): Single<RepoEntity>

    @Query("DELETE FROM favRepos WHERE uid LIKE :uid")
    fun clearFavorites(uid: String): Completable
}