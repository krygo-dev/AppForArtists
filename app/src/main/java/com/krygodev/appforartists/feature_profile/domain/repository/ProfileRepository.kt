package com.krygodev.appforartists.feature_profile.domain.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUserData(uid: String) : Flow<Resource<UserModel>>

    fun getUserImagesOrFavorites(listOfId: List<String>) : Flow<Resource<List<ImageModel>>>

    fun setOrUpdateUserData(user: UserModel) : Flow<Resource<Void>>

    fun uploadUserPhoto(uid: String, photoUri: Uri) : Flow<Resource<Uri>>

    fun getCurrentUser() : FirebaseUser?
}