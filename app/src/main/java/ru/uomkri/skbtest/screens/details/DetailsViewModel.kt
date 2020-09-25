package ru.uomkri.skbtest.screens.details

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.uomkri.skbtest.repo.Repository
import ru.uomkri.skbtest.repo.net.Repo

class DetailsViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _repo = repository.repo
    val repo: LiveData<Repo>
        get() = _repo

    private val _error = repository.error
    val error: LiveData<String>
        get() = _error

    private val _isInFavorites = repository.isInFavorites
    val isInFavorites: LiveData<Boolean>
        get() = _isInFavorites

    fun getRepo(owner: String, repo: String) {
        repository.getRepo(owner, repo)
    }

    fun isRepoInFavorites(repoId: Int, uid: String) {
        Log.e("uidVM", uid)
        repository.isRepoInFavorites(repoId, uid)
        Log.e("fav", "called from vm")
    }

    fun insertRepoIntoFavorites(repo: Repo, uid: String) {
        repository.insertRepoToFavorites(repo, uid)
    }

    fun deleteRepoFromFavorites(repoId: Int, uid: String) {
        repository.deleteFromFavorites(repoId, uid)
    }

    fun clearError() {
        repository.clearError()
    }

    fun clearSelectedRepo() {
        repository.clearSelectedRepo()
    }

}