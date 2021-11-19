package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.google.firebase.auth.AuthResult
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInWithEmailAndPassword(
    private val _repository: AuthenticationRepository
) {

    operator fun invoke(email: String, password: String): Flow<Resource<AuthResult>> {
        if (email.isBlank() || password.isBlank()) {
            return flow {
                emit(Resource.Error(message = "Wprowadź email i hasło!"))
            }
        }
        return _repository.signInWithEmailAndPass(email, password)
    }
}