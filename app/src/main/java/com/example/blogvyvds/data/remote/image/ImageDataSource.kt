package com.example.blogvyvds.data.remote.image

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class ImageDataSource {

    suspend fun uploadImage(
        userId: String,
        postId: String,
        imageBitmap: Bitmap
    ) {
        val imageRef = FirebaseStorage.getInstance().reference.child("postImages/$userId/$postId")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        imageRef.putBytes(baos.toByteArray()).await()
    }

}