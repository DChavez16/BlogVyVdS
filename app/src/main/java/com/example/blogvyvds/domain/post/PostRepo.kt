package com.example.blogvyvds.domain.post

import android.content.Context
import android.net.Uri
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.model.Post
import com.google.android.gms.tasks.Task

interface PostRepo {
    suspend fun uploadPost(post: Post, imageUri: Uri?, fileUri: Uri?, context: Context)

    suspend fun getPostList(): List<Post>
}