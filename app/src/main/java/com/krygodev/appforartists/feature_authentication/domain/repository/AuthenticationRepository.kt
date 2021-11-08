package com.krygodev.appforartists.feature_authentication.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthCredential

interface AuthenticationRepository {

    suspend fun signInWithEmailAndPass(email: String, password: String) : Task<AuthResult>

    suspend fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential)

    suspend fun signUpWithEmailAndPass(email: String, password: String)

    suspend fun resetAccountPassword(email: String)

    suspend fun signOut()
}