package com.example.blogvyvds.domain.image

import android.graphics.Bitmap

interface ImageRepo {
    suspend fun uploadImage(userId: String, postId: String, imageBitmap: Bitmap)
}