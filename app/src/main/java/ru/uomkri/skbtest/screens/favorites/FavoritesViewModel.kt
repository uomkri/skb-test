package ru.uomkri.skbtest.screens.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.uomkri.skbtest.repo.Repository
import ru.uomkri.skbtest.repo.net.Repo

class FavoritesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _favRepos = repository.favRepoList
    val favRepos: LiveData<List<Repo>>
        get() = _favRepos

    private val _error = repository.error
    val error: LiveData<String>
        get() = _error

    fun getFavRepos(uid: String) {
        repository.getFavRepos(uid)
    }

    fun filterByName(query: String): List<Repo> {
        return favRepos.value!!.filter {
            it.name.contains(query)
        }
    }

    fun clearFavorites(uid: String) {
        repository.clearFavorites(uid)
    }

    fun clearError() {
        repository.clearError()
    }

}