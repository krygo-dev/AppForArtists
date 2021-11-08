package com.krygodev.appforartists.feature_authentication.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await


@ExperimentalCoroutinesApi
class AuthenticationRepositoryImpl(
    private val _firebaseAuth: FirebaseAuth,
    private val _firebaseFirestore: FirebaseFirestore
) : AuthenticationRepository {

    //private val _firebaseAuth = FirebaseAuth.getInstance()
    //private val _firebaseFirestore = FirebaseFirestore.getInstance()


    override suspend fun signInWithEmailAndPass(email: String, password: String): AuthResult {
        return _firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }


    override suspend fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential): AuthResult {
        TODO("Not implemented yet.")
    }


    override suspend fun signUpWithEmailAndPass(email: String, password: String): AuthResult {
        return _firebaseAuth.createUserWithEmailAndPassword(email, password).await().also { result ->
            val newUser = User(result.user?.uid, result.user?.email)
            addNewUserToFirestore(newUser)
            _firebaseAuth.currentUser?.sendEmailVerification()?.await()
        }
    }


    override suspend fun resetAccountPassword(email: String): Void? {
        return _firebaseAuth.sendPasswordResetEmail(email).await()
    }


    override suspend fun signOut() {
        return _firebaseAuth.signOut()
    }


    private suspend fun addNewUserToFirestore(user: User) {
        _firebaseFirestore.collection(Constants.USER_COLLECTION).document(user.uid!!).set(user)
            .await()
    }
}