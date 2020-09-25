package ru.uomkri.skbtest.repo.net

import androidx.paging.PageKeyedDataSource
import ru.uomkri.skbtest.repo.Repository

class RepoRemoteDataSource(
    private val repository: Repository,
    private var query: String
) : PageKeyedDataSource<Int, Repo>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Repo>
    ) {
        repository.runRemoteQuery(query, 1) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        val page = params.key

        repository.runRemoteQuery(query, page) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        val page = params.key

        repository.runRemoteQuery(query, page) {
            callback.onResult(it, page - 1)
        }
    }

    fun refresh() {
        this.invalidate()
    }

}