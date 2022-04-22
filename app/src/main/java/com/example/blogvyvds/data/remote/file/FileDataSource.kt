package com.example.blogvyvds.data.remote.file

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

class FileDataSource {

    suspend fun uploadFile(
        userId: String,
        postId: String,
        file: File
    ) {
        val fileRef = FirebaseStorage.getInstance().reference.child("postFiles/$userId/$postId")

        fileRef.putFile(Uri.fromFile(file)).await()
    }

}