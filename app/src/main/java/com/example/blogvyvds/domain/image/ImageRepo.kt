package com.example.blogvyvds.domain.image

import android.net.Uri

interface ImageRepo {
    suspend fun uploadImage(userId: String, postId: String, imageUri: Uri)
}