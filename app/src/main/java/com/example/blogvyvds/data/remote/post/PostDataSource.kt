package com.example.blogvyvds.data.remote.post

import android.content.Context
import android.net.Uri
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.core.getFileName
import com.example.blogvyvds.data.model.Post
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostDataSource {

    suspend fun uploadPost(
        post: Post,
        imageUri: Uri?,
        fileUri: Uri?,
        context: Context
    ) {
        post.id = FirebaseDatabase.getInstance().reference.child("Post").push().key ?: ""

        imageUri?.let {
            post.imageName = it.getFileName(context)
            post.imageUrl = uploadImage(post.userId, post.id, post.imageName!!, it)
        }

        fileUri?.let {
            post.fileName = it.getFileName(context)
            post.fileUrl = uploadFile(post.userId, post.id, post.fileName!!, it)
        }

        FirebaseDatabase
                .getInstance()
                .getReference("Post")
                .child(post.id)
                .setValue(post)
                .await()
    }

    suspend fun getPostList(): List<Post> {
        val postList = mutableListOf<Post>()

        withContext(Dispatchers.IO) {
            val querySnapshot = FirebaseDatabase.getInstance().reference.child("Post").get().await()
            for(post in querySnapshot.children) {
                post.getValue(Post::class.java)?.let { postElement ->
                    postList.add(postElement)
                }
            }
        }

        return postList
    }

    private suspend fun uploadImage(userId: String, postId: String, imageName: String, imageUri: Uri): String {
        val imageRef = FirebaseStorage.getInstance().reference.child("postImages/$userId/$postId/$imageName")

        imageRef.putFile(imageUri).await()

        return imageRef.downloadUrl.await().toString()
    }

    private suspend fun uploadFile(userId: String, postId: String, fileName: String, fileUri: Uri): String {
        val fileRef = FirebaseStorage.getInstance().reference.child("postFile/$userId/$postId/$fileName")

        fileRef.putFile(fileUri).await()

        return fileRef.downloadUrl.await().toString()
    }
}