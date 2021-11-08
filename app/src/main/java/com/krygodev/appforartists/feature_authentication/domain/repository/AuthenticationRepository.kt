package com.krygodev.appforartists.feature_authentication.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthCredential

interface AuthenticationRepository {

    suspend fun signInWithEmailAndPass(email: String, password: String): AuthResult

    suspend fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential): AuthResult

    suspend fun signUpWithEmailAndPass(email: String, password: String): AuthResult

    suspend fun resetAccountPassword(email: String): Void?

    suspend fun signOut()
}