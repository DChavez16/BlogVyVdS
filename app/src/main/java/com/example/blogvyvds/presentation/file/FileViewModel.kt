package com.example.blogvyvds.presentation.file

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.domain.file.FileRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class FileViewModel(private val repo: FileRepo): ViewModel() {

    fun uploadFile(userId: String, postId: String, fileUri: Uri) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repo.uploadFile(userId, postId, fileUri)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

}


class FileViewModelFactory(private val repo: FileRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FileViewModel(repo) as T
    }
}