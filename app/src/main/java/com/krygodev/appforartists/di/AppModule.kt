package com.krygodev.appforartists.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.krygodev.appforartists.feature_authentication.data.repository.AuthenticationRepositoryImpl
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.domain.use_case.*
import com.krygodev.appforartists.feature_profile.data.repository.ProfileRepositoryImpl
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import com.krygodev.appforartists.feature_profile.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

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
            resetAccountPassword = ResetAccountPassword(repository),
            signOut = SignOut(repository)
        )
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): ProfileRepository {
        return ProfileRepositoryImpl(firebaseFirestore, firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideProfileUseCases(repository: ProfileRepository): ProfileUseCases {
        return ProfileUseCases(
            getUserData = GetUserData(repository),
            getUserImages = GetUserImages(repository),
            setOrUpdateUserData = SetOrUpdateUserData(repository),
            uploadUserPhoto = UploadUserPhoto(repository)
        )
    }
}