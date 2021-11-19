package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ResetAccountPassword(
    private val _repository: AuthenticationRepository
) {

    operator fun invoke(email: String): Flow<Resource<Void>> {
        if (email.isBlank()) {
            return flow {
                emit(Resource.Error("Wprowadź adres email!"))
            }
        }

        return _repository.resetAccountPassword(email)
    }
}