package com.example.blogvyvds.domain.image

import android.net.Uri
import com.example.blogvyvds.data.remote.image.ImageDataSource

class ImageRepoImpl(private val dataSource: ImageDataSource): ImageRepo {
    override suspend fun uploadImage(userId: String, postId: String, imageUri: Uri) =
        dataSource.uploadImage(userId, postId, imageUri)
}