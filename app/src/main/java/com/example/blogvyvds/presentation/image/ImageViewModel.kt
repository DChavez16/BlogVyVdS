package com.example.blogvyvds.presentation.image

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.domain.image.ImageRepo
import kotlinx.coroutines.Dispatchers

class ImageViewModel(private val repo: ImageRepo): ViewModel() {

    fun uploadImage(userId: String, postId: String, imageUri: Uri) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repo.uploadImage(userId, postId, imageUri)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

}

class ImageViewModelFactory(private val repo: ImageRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageViewModel(repo) as T
    }
}