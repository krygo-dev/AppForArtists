package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.google.firebase.auth.GoogleAuthCredential
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import kotlinx.coroutines.flow.Flow

class SignInWithGoogle(
    private val repository: AuthenticationRepository
) {

    operator fun invoke(googleAuthCredential: GoogleAuthCredential): Flow<AuthenticationState<Any>> {
        return repository.signInWithGoogle(googleAuthCredential)
    }
}