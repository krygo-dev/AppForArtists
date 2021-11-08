package com.krygodev.appforartists.feature_authentication.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@ExperimentalCoroutinesApi
class AuthenticationRepositoryImpl @Inject constructor(
    private val _firebaseAuth: FirebaseAuth,
    private val _firebaseFirestore: FirebaseFirestore
) : AuthenticationRepository {

    //private val _firebaseAuth = FirebaseAuth.getInstance()
    //private val _firebaseFirestore = FirebaseFirestore.getInstance()


    override suspend fun signInWithEmailAndPass(email: String, password: String): Task<AuthResult> {
        return _firebaseAuth.signInWithEmailAndPassword(email, password)
    }


    override suspend fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential) {
        TODO("Not yet implemented")
    }


    override suspend fun signUpWithEmailAndPass(email: String, password: String) {
        TODO("Not yet implemented")
    }


    override suspend fun resetAccountPassword(email: String) {
        TODO("Not yet implemented")
    }


    override suspend fun signOut() {
        TODO("Not yet implemented")
    }


    private suspend fun addNewUserToFirestore(user: User) {
        _firebaseFirestore.collection(Constants.USER_COLLECTION).document(user.uid!!).set(user)
            .await()
    }
}