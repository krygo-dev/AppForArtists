package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import kotlinx.coroutines.flow.Flow

class SignOut(
    private val repository: AuthenticationRepository
) {

    operator fun invoke(): Flow<AuthenticationState<Any>> {
        return repository.signOut()
    }
}