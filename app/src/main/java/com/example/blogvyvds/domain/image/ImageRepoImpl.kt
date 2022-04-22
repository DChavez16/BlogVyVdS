package com.example.blogvyvds.domain.image

import android.graphics.Bitmap
import com.example.blogvyvds.data.remote.image.ImageDataSource

class ImageRepoImpl(private val dataSource: ImageDataSource): ImageRepo {
    override suspend fun uploadImage(userId: String, postId: String, imageBitmap: Bitmap) =
        dataSource.uploadImage(userId, postId, imageBitmap)
}