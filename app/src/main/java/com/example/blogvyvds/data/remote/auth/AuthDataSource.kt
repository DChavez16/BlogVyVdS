package com.example.blogvyvds.data.remote.auth

import android.net.Uri
import com.example.blogvyvds.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthDataSource {

    suspend fun signUp(email: String, password: String, username: String, profilePicUri: Uri): FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        authResult.user?.uid?.let { uid ->
            val imageUrl = uploadProfilePic(profilePicUri, uid)

            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .set(User(email, username, imageUrl))
                .await()
        }

        return authResult.user
    }

    private suspend fun uploadProfilePic(profilePicUri: Uri, userId: String): String {
        val imageRef = FirebaseStorage.getInstance().reference.child("userProfilePics/$userId/profile_image")

        imageRef.putFile(profilePicUri).await()

        return imageRef.downloadUrl.await().toString()
    }

    suspend fun singIn(email: String, password: String): FirebaseUser? {
        val authResult = FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .await()

        return authResult.user
    }
}