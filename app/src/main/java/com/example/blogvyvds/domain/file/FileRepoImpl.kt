package com.example.blogvyvds.domain.file

import com.example.blogvyvds.data.remote.file.FileDataSource
import java.io.File

class FileRepoImpl(private val dataSource: FileDataSource): FileRepo {
    override suspend fun uploadFile(userId: String, postId: String, file: File) =
        dataSource.uploadFile(userId, postId, file)
}