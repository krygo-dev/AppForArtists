package com.krygodev.appforartists.feature_authentication.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException


class AuthenticationRepositoryImpl(
    private val _firebaseAuth: FirebaseAuth,
    private val _firebaseFirestore: FirebaseFirestore
) : AuthenticationRepository {

    override fun signInWithEmailAndPass(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (result.user!!.isEmailVerified) {
                emit(Resource.Success(result))
            } else {
                emit(Resource.Error(message = "Najpierw zweryfikuj swój adres email!"))
            }

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }


    override fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential): Flow<Resource<AuthResult>> {
        TODO("Not implemented yet.")
    }


    override fun signUpWithEmailAndPass(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            result.user?.let { user ->
                val newUser = User(uid = user.uid, email = user.email)
                addNewUserToFirestore(newUser)
                user.sendEmailVerification()
            }

            _firebaseAuth.signOut()

            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }


    override fun resetAccountPassword(email: String): Flow<Resource<Void>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun signOut(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseAuth.signOut()
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Something went wrong!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection!"))
        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }


    private suspend fun addNewUserToFirestore(user: User) {
        _firebaseFirestore.collection(Constants.USER_COLLECTION).document(user.uid!!).set(user)
            .await()
    }
}