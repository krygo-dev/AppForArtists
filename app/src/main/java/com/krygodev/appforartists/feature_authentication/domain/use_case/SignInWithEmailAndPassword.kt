package com.krygodev.appforartists.feature_authentication.domain.use_case

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.lang.Exception

class SignInWithEmailAndPassword(
    private val _repository: AuthenticationRepository
) {

    operator fun invoke(email: String, password: String): Flow<Resource<Task<AuthResult>>> = flow{
        try {
            emit(Resource.Loading())
            val result = _repository.signInWithEmailAndPass(email, password)
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage!!))
        }
    }
}