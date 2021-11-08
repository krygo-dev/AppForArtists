package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthCredential
import com.google.rpc.context.AttributeContext
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInWithGoogle(
    private val _repository: AuthenticationRepository
) {

    operator fun invoke(googleAuthCredential: GoogleAuthCredential): Flow<Resource<AuthResult>> = flow {
        TODO("Not implemented yet.")
    }
}