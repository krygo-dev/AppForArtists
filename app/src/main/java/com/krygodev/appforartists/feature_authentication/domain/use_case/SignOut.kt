package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class SignOut(
    private val _repository: AuthenticationRepository
) {
    operator fun invoke() : Flow<Resource<Unit>> {
        return _repository.signOut()
    }
}