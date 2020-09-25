package ru.uomkri.skbtest.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.rxjava3.EmptyResultSetException
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.uomkri.skbtest.repo.db.RepoDao
import ru.uomkri.skbtest.repo.db.RepoEntity
import ru.uomkri.skbtest.repo.net.*
import ru.uomkri.skbtest.utils.asDatabaseModel
import ru.uomkri.skbtest.utils.asDomainModel
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: RepoDao,
    private val remoteDataSource: ReposResourceService
) {
    val error = MutableLiveData<String>()
    val repoList = MutableLiveData<List<Repo>>()
    val repo = MutableLiveData<Repo>()
    val isInFavorites = MutableLiveData<Boolean>()
    val favRepoList = MutableLiveData<List<Repo>>()

    fun clearError() {
        error.postValue(null)
    }

    fun clearSelectedRepo() {
        repo.postValue(null)
    }

    fun runRemoteQuery(query: String, page: Int, callback: (List<Repo>) -> Unit) {
        if (query.isNotEmpty()) {
            remoteDataSource.getSearchResult(query = query, page = page)
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<SearchResponse> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(t: SearchResponse?) {
                        callback(t!!.items)
                        repoList.postValue(t.items)
                    }

                    override fun onError(e: Throwable?) {
                        Log.e("err", e?.message)
                        error.postValue(e!!.message)
                    }

                })


        } else {
            remoteDataSource.getAllRepos(0)
                .subscribeOn(Schedulers.io())
                .subscribe (object: Observer<List<Repo>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(t: List<Repo>?) {
                        callback(t!!)
                        repoList.postValue(t)
                    }

                    override fun onError(e: Throwable?) {
                        error.postValue(e!!.message)
                    }

                })
        }
    }

    fun getRepo(owner: String, repo: String) {
        remoteDataSource.getRepo(owner, repo)
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<Repo> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable?) {

                }

                override fun onNext(t: Repo?) {
                    this@Repository.repo.postValue(t!!)
                }

                override fun onError(e: Throwable?) {
                    Log.e("err", e!!.message)
                    error.postValue(e.message)
                }
            })
    }

    fun isRepoInFavorites(repoId: Int, uid: String) {
        localDataSource.getRepoFromDatabase(repoId, uid)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<RepoEntity> {
                override fun onSubscribe(d: Disposable?) {
                    Log.e("db", "on sub")
                }

                override fun onError(e: Throwable?) {
                    Log.e("err", e!!.message)
                    if (e is EmptyResultSetException) isInFavorites.postValue(false)
                }

                override fun onSuccess(t: RepoEntity?) {
                    Log.e("db", "on next")
                    Log.e("fav", t.toString())
                    if (t != null) isInFavorites.postValue(true)
                }
            })
    }

    fun deleteFromFavorites(repoId: Int, uid: String) {
        localDataSource.deleteFromFavorites(repoId, uid)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun insertRepoToFavorites(repo: Repo, uid: String) {
        val entity = asDatabaseModel(repo, uid)
        localDataSource.insertToFavorites(entity)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.e("ins", "done")
                }

                override fun onSubscribe(d: Disposable?) {

                }

                override fun onError(e: Throwable?) {
                    Log.e("ins", e!!.message)
                }

            })
    }

    fun clearFavorites(uid: String) {
        localDataSource.clearFavorites(uid)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getFavRepos(uid: String) {
        localDataSource.getAllFavRepos(uid)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<RepoEntity>> {
                override fun onError(e: Throwable?) {
                    Log.e("db", "${e!!.message}")
                }

                override fun onSuccess(t: List<RepoEntity>?) {
                    favRepoList.postValue(t!!.asDomainModel())
                }

                override fun onSubscribe(d: Disposable?) {

                }

            })
    }
}