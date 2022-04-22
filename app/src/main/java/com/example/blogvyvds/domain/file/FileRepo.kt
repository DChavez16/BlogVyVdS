package com.example.blogvyvds.domain.file

import java.io.File

interface FileRepo {
    suspend fun uploadFile(userId: String, postId: String, file: File)
}