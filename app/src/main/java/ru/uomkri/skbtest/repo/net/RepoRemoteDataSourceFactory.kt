package ru.uomkri.skbtest.repo.net

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import ru.uomkri.skbtest.repo.Repository

class RepoRemoteDataSourceFactory(
    private val repository: Repository,
    private var query: String
) : DataSource.Factory<Int, Repo>() {
    val dataSource = MutableLiveData<RepoRemoteDataSource>()

    override fun create(): DataSource<Int, Repo> {
        val dataSource = RepoRemoteDataSource(repository, query)
        this.dataSource.postValue(dataSource)
        return dataSource
    }

    fun getQuery() = query
    fun getSource() = dataSource.value

    fun updateQuery(query: String) {
        this.query = query
        getSource()?.refresh()
    }
}