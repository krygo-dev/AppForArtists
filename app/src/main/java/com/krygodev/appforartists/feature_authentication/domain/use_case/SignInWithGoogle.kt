package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthCredential
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class SignInWithGoogle(
    private val _repository: AuthenticationRepository
) {

    operator fun invoke(googleAuthCredential: GoogleAuthCredential): Flow<Resource<AuthResult>> {
        TODO()
    }
}