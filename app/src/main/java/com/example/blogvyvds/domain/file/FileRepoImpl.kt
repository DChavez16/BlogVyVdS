package com.example.blogvyvds.domain.file

import android.net.Uri
import com.example.blogvyvds.data.remote.file.FileDataSource

class FileRepoImpl(private val dataSource: FileDataSource): FileRepo {
    override suspend fun uploadFile(userId: String, postId: String, fileUri: Uri) =
        dataSource.uploadFile(userId, postId, fileUri)
}