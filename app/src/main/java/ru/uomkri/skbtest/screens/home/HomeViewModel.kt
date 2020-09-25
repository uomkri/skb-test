package ru.uomkri.skbtest.screens.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import ru.uomkri.skbtest.repo.Repository
import ru.uomkri.skbtest.repo.net.Repo
import ru.uomkri.skbtest.repo.net.RepoRemoteDataSourceFactory

class HomeViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val dataSourceFactory: RepoRemoteDataSourceFactory
) : ViewModel() {

    val _error = repository.error
    val error: LiveData<String>
        get() = _error

    val _repoList = repository.repoList
    val repoList: LiveData<List<Repo>>
        get() = _repoList

    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setPrefetchDistance(5)
        .setEnablePlaceholders(false)
        .build()
    private val reposLiveData = LivePagedListBuilder(dataSourceFactory, config).build()

    fun getRepos() = reposLiveData

    fun fetchRepos(query: String) {
        return dataSourceFactory.updateQuery(query.trim())
    }

    fun clearError() {
        repository.clearError()
    }

}