package com.krygodev.appforartists.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.krygodev.appforartists.feature_authentication.data.repository.AuthenticationRepositoryImpl
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(firebaseAuth, firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(repository: AuthenticationRepository): AuthenticationUseCases {
        return AuthenticationUseCases(
            signInWithEmailAndPassword = SignInWithEmailAndPassword(repository),
            signInWithGoogle = SignInWithGoogle(repository),
            signUpWithEmailAndPassword = SignUpWithEmailAndPassword(repository),
            resetAccountPassword = ResetAccountPassword(repository)
        )
    }
}