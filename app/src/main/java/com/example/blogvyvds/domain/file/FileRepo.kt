package com.example.blogvyvds.domain.file

import android.net.Uri

interface FileRepo {
    suspend fun uploadFile(userId: String, postId: String, fileUri: Uri)
}