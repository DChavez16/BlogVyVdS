package com.example.blogvyvds.presentation.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.model.User
import com.example.blogvyvds.domain.user.UserRepository
import kotlinx.coroutines.Dispatchers

class UserViewModel(private val repo: UserRepository): ViewModel() {

    fun getUser() = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repo.getUser()))
        }catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun saveUser(user: User) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repo.saveUser(user)))
        }catch (e: Exception) {
            Result.Failure(e)
        }
    }

    fun getRemoteUser() = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repo.getRemoteUser()))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun updateRemoteUser(user: User, profilePicUri: Uri) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repo.updateRemoteUser(user, profilePicUri)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}


class UserViewModelFactory(private val repo: UserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(repo) as T
    }
}