package com.krygodev.appforartists.feature_authentication.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthCredential
import com.krygodev.appforartists.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun signInWithEmailAndPass(email: String, password: String): Flow<Resource<AuthResult>>

    fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential): Flow<Resource<AuthResult>>

    fun signUpWithEmailAndPass(email: String, password: String, username: String): Flow<Resource<AuthResult>>

    fun resetAccountPassword(email: String): Flow<Resource<Void>>

    fun signOut(): Flow<Resource<Unit>>
}