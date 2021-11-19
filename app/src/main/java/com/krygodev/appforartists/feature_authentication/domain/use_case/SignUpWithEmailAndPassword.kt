package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.google.firebase.auth.AuthResult
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignUpWithEmailAndPassword(
    private val _repository: AuthenticationRepository
) {

    operator fun invoke(
        email: String,
        password: String,
        repeatPassword: String
    ): Flow<Resource<AuthResult>> {
        if (email.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
            return flow {
                emit(Resource.Error(message = "Wypełnij wszystkie pola!"))
            }
        }

        if (password != repeatPassword) {
            return flow {
                emit(Resource.Error(message = "Hasła nie są takie same!"))
            }
        }

        return _repository.signUpWithEmailAndPass(email, password)
    }
}