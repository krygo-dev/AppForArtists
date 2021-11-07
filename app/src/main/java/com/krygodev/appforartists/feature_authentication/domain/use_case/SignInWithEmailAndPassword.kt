package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import kotlinx.coroutines.flow.Flow

class SignInWithEmailAndPassword(
    private val repository: AuthenticationRepository
) {

    operator fun invoke(email: String, password: String): Flow<AuthenticationState<Any>> {
        return repository.signInWithEmailAndPass(email, password)
    }
}