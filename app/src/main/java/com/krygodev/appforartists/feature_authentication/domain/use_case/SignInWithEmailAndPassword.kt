package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.google.firebase.auth.AuthResult
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class SignInWithEmailAndPassword(
    private val _repository: AuthenticationRepository
) {

    operator fun invoke(email: String, password: String): Flow<Resource<AuthResult>> = flow {
        try {
            emit(Resource.Loading())
            if (email.isBlank()) throw Exception("Wprowadź adres email.")
            if (password.isBlank()) throw Exception("Wprowadź hasło.")
            val result = _repository.signInWithEmailAndPass(email, password)
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Wystąpił niespodziewany błąd."))
        } catch (e: IOException) {
            emit(Resource.Error("Nie udało się połączyć z serwerem. Sprawdź swoje połączenie z internetem."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage!!))
        }
    }
}