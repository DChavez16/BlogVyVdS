package com.example.blogvyvds.data.remote.user

import android.net.Uri
import android.util.Log
import com.example.blogvyvds.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RemoteUserDataSource {

    suspend fun getRemoteUser(): User {
        var user: User
        val userID = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        withContext(Dispatchers.IO) {
            val userQuerySnapshot = FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userID)
                .get()
                .await()

            user = userQuerySnapshot.toObject(User::class.java)!!
        }

        return user
    }

    suspend fun updateRemoteUser(
        user: User,
        profilePicUri: Uri
    ): User {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            user.photo_url = uploadProfilePic(profilePicUri, it)

            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(it)
                .set(user)
                .await()
        }

        return user
    }

    private suspend fun uploadProfilePic(profilePicUri: Uri, userId: String): String {
        val imageRef = FirebaseStorage.getInstance().reference.child("userProfilePics/$userId/profile_image")

        return imageRef.downloadUrl.await().toString()
    }
}