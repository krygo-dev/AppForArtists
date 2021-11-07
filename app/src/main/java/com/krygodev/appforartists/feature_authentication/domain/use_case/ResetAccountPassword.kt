package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import kotlinx.coroutines.flow.Flow

class ResetAccountPassword(
    private val repository: AuthenticationRepository
) {

    operator fun invoke(email: String): Flow<AuthenticationState<Any>> {
        return repository.resetAccountPassword(email)
    }
}