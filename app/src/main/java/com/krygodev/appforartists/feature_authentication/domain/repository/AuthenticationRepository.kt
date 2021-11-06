package com.krygodev.appforartists.feature_authentication.domain.repository

import com.google.firebase.auth.GoogleAuthCredential
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun singInWithEmailAndPass(email: String, password: String) : Flow<AuthenticationState<Any>>

    fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential) : Flow<AuthenticationState<Any>>

    fun signUpWithEmailAndPass(email: String, password: String) : Flow<AuthenticationState<Any>>

    fun resetAccountPassword(email: String) : Flow<AuthenticationState<Any>>

    fun signOut() : Flow<AuthenticationState<Any>>
}