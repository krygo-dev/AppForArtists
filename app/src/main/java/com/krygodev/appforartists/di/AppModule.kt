package com.krygodev.appforartists.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.krygodev.appforartists.feature_authentication.data.repository.AuthenticationRepositoryImpl
import com.krygodev.appforartists.feature_authentication.domain.repository.AuthenticationRepository
import com.krygodev.appforartists.feature_authentication.domain.use_case.*
import com.krygodev.appforartists.feature_image.data.repository.HomeRepositoryImpl
import com.krygodev.appforartists.feature_image.data.repository.ImageRepositoryImpl
import com.krygodev.appforartists.feature_image.domain.repository.HomeRepository
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import com.krygodev.appforartists.feature_image.domain.use_case.home.*
import com.krygodev.appforartists.feature_image.domain.use_case.image.*
import com.krygodev.appforartists.feature_profile.data.repository.ChatRepositoryImpl
import com.krygodev.appforartists.feature_profile.data.repository.ProfileRepositoryImpl
import com.krygodev.appforartists.feature_profile.domain.repository.ChatRepository
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import com.krygodev.appforartists.feature_profile.domain.use_case.chat.*
import com.krygodev.appforartists.feature_profile.domain.use_case.profile.*
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
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): ProfileRepository {
        return ProfileRepositoryImpl(firebaseAuth, firebaseFirestore, firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideProfileUseCases(repository: ProfileRepository): ProfileUseCases {
        return ProfileUseCases(
            getUserData = GetUserData(repository),
            getUserImages = GetUserImages(repository),
            getUserFavorites = GetUserFavorites(repository),
            setOrUpdateUserData = SetOrUpdateUserData(repository),
            uploadUserPhoto = UploadUserPhoto(repository),
            getCurrentUser = GetCurrentUser(repository)
        )
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): ImageRepository {
        return ImageRepositoryImpl(firebaseFirestore, firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideImageUseCases(repository: ImageRepository): ImageUseCases {
        return ImageUseCases(
            getImageById = GetImageById(repository),
            getImagesByTag = GetImagesByTag(repository),
            getUsersByUsername = GetUsersByUsername(repository),
            addImage = AddImage(repository),
            editImage = EditImage(repository),
            deleteImage = DeleteImage(repository),
            getImageComments = GetImageComments(repository),
            addComment = AddComment(repository),
            deleteComment = DeleteComment(repository)
        )
    }

    @Provides
    @Singleton
    fun provideHomeRepository(
        firebaseFirestore: FirebaseFirestore
    ): HomeRepository {
        return HomeRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideHomeUseCases(repository: HomeRepository): HomeUseCases {
        return HomeUseCases(
            getRandomImage = GetRandomImage(repository),
            getMostLikedImages = GetMostLikedImages(repository),
            getBestRatedImages = GetBestRatedImages(repository),
            getRecentlyAddedImages = GetRecentlyAddedImages(repository),
            getBestRatedUsers = GetBestRatedUsers(repository)
        )
    }

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun provideChatRepository(
        firebaseFirestore: FirebaseFirestore
    ): ChatRepository {
        return ChatRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideChatUseCases(repository: ChatRepository): ChatUseCases {
        return ChatUseCases(
            getUserChatrooms = GetUserChatrooms(repository),
            getChatroomByUsersUid = GetChatroomByUsersUid(repository),
            getMessages = GetMessages(repository),
            sendMessage = SendMessage(repository)
        )
    }
}