package com.example.blogvyvds.data.remote.post

import android.util.Log
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.model.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase

class PostDataSource {

    suspend fun uploadPost(
        userName: String,
        userImg: String,
        description: String,
        imageUrl: String,
        fileUrl: String,
        date: String,
        time: String
    ) : Result<Task<Void>> {
        val id = FirebaseDatabase.getInstance().reference.child("Post").push().key ?: ""

        val postResult = FirebaseDatabase
                .getInstance()
                .getReference("Post")
                .child(id)
                .setValue(Post(id, userName, userImg, description, imageUrl, fileUrl, date, time))

        return Result.Success(postResult)
    }
}