package com.example.blogvyvds.data.remote.image

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ImageDataSource {

    suspend fun uploadImage(
        userId: String,
        postId: String,
        imageUri: Uri
    ) {
        val imageRef = FirebaseStorage.getInstance().reference.child("postImages/$userId/$postId")

        imageRef.putFile(imageUri).await()
    }

}