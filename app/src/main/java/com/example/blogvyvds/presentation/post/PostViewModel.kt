package com.example.blogvyvds.presentation.post

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.model.Post
import com.example.blogvyvds.domain.post.PostRepo
import kotlinx.coroutines.Dispatchers

class PostViewModel(private val repo: PostRepo): ViewModel() {

    fun uploadPost(post: Post, imageUri: Uri?, fileUri: Uri?, context: Context) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.uploadPost(post, imageUri, fileUri, context)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun getPostList() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repo.getPostList()))
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