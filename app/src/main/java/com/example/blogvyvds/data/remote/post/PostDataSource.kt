package com.example.blogvyvds.data.remote.post

import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.model.Post
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class PostDataSource {

    suspend fun uploadPost(
        userName: String,
        userImg: String,
        description: String,
        userId: String,
        date: String,
        time: String,
        imgBool: Boolean,
        fileBool: Boolean
    ) : Result<String> {
        val id = FirebaseDatabase.getInstance().reference.child("Post").push().key ?: ""

        val imageUrl = if(imgBool) getImageUrl(userId, id) else ""
        val fileUrl = if(fileBool) getFileUrl(userId, id) else ""

        val postResult = FirebaseDatabase
                .getInstance()
                .getReference("Post")
                .child(id)
                .setValue(Post(id, userName, userImg, description, imageUrl, fileUrl, date, time))

        return Result.Success(id)
    }

    private fun getImageUrl(userId: String, postId: String): String {
        return "${FirebaseStorage.getInstance().reference.path}/postImages/$userId/$postId"
    }

    private fun getFileUrl(userId: String, postId: String): String {
        return ""
    }
}