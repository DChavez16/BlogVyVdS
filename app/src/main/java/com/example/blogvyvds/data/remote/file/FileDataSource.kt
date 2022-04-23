package com.example.blogvyvds.data.remote.file

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FileDataSource {

    suspend fun uploadFile(
        userId: String,
        postId: String,
        fileUri: Uri
    ) {
        val fileRef = FirebaseStorage.getInstance().reference.child("postFiles/$userId/$postId")

        fileRef.putFile(fileUri).await()
    }

}