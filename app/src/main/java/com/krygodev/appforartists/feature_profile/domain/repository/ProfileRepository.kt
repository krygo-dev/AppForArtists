package com.krygodev.appforartists.feature_profile.domain.repository

import android.net.Uri
import com.krygodev.appforartists.core.domain.model.Painting
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUserData(uid: String) : Flow<Resource<User>>

    fun getUserPaintings(user: User) : Flow<Resource<List<Painting>>>

    fun setOrUpdateUserData(user: User) : Flow<Resource<Void>>

    fun uploadUserPhoto(uid: String, photoUri: Uri) : Flow<Resource<Uri>>
}