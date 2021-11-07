package com.krygodev.appforartists.feature_authentication.data.repository

import android.app.Activity
import android.content.Context
import android.provider.Settings.Global.getString
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.krygodev.appforartists.R
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.security.AccessController.getContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext


@ExperimentalCoroutinesApi
class AuthenticationRepositoryImpl @Inject constructor(
    private val _firebaseAuth: FirebaseAuth,
    private val _firebaseFirestore: FirebaseFirestore
) : AuthenticationRepository {

    //private val _firebaseAuth = FirebaseAuth.getInstance()
    //private val _firebaseFirestore = FirebaseFirestore.getInstance()


    override fun signInWithEmailAndPass(
        email: String,
        password: String
    ): Flow<AuthenticationState<Any>> =
        flow<AuthenticationState<Any>> {
            emit(AuthenticationState.loading())
            val user = _firebaseAuth.signInWithEmailAndPassword(email, password).await()

            user?.let {
                if (_firebaseAuth.currentUser?.isEmailVerified!!) {
                    emit(AuthenticationState.success("Successfully logged in."))
                } else {
                    _firebaseAuth.currentUser?.sendEmailVerification()?.await()
                    emit(AuthenticationState.error("Verify your email first."))
                }
            }
        }.catch {
            emit(AuthenticationState.error(it.localizedMessage!!))
        }.flowOn(Dispatchers.IO)


    override fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential): Flow<AuthenticationState<Any>> {
        TODO("Not yet implemented")
    }


    override fun signUpWithEmailAndPass(
        email: String,
        password: String
    ): Flow<AuthenticationState<Any>> = flow<AuthenticationState<Any>> {
        emit(AuthenticationState.loading())
        val result = _firebaseAuth.createUserWithEmailAndPassword(email, password).await()

        result.user?.let {
            val newUser = User(result.user?.uid, result.user?.email)

            addNewUserToFirestore(newUser)

            _firebaseAuth.currentUser?.sendEmailVerification()?.await()

            emit(AuthenticationState.success("Account created. Verify your email."))
        }
    }.catch {
        emit(AuthenticationState.error(it.localizedMessage!!))
    }.flowOn(Dispatchers.IO)


    override fun resetAccountPassword(email: String): Flow<AuthenticationState<Any>> =
        flow<AuthenticationState<Any>> {
            emit(AuthenticationState.loading())
            _firebaseAuth.sendPasswordResetEmail(email).await()
            emit(AuthenticationState.success("Reset password email sent."))
        }.catch {
            emit(AuthenticationState.error(it.localizedMessage!!))
        }.flowOn(Dispatchers.IO)


    override fun signOut(): Flow<AuthenticationState<Any>> = flow<AuthenticationState<Any>> {
        emit(AuthenticationState.loading())
        _firebaseAuth.signOut()
        emit(AuthenticationState.success("Successfully logged out."))
    }.catch {
        emit(AuthenticationState.error(it.localizedMessage!!))
    }.flowOn(Dispatchers.IO)


    private suspend fun addNewUserToFirestore(user: User) {
        _firebaseFirestore.collection(Constants.USER_COLLECTION).document(user.uid!!).set(user)
            .await()
    }
}