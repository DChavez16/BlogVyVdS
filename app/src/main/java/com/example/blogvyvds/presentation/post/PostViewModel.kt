package com.example.blogvyvds.presentation.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.domain.post.PostRepo
import kotlinx.coroutines.Dispatchers

class PostViewModel(private val repo: PostRepo): ViewModel() {

    fun uploadPost(
        userName: String,
        userImg: String,
        description: String,
        imageUrl: String,
        fileUrl: String,
        date: String,
        time: String
    ) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(repo.uploadPost(userName, userImg, description, imageUrl, fileUrl, date, time))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}


class PostViewModelFactory(private val repo: PostRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostViewModel(repo) as T
    }
}