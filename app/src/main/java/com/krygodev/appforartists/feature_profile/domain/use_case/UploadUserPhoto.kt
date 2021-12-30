package com.krygodev.appforartists.feature_profile.domain.use_case

import android.net.Uri
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class UploadUserPhoto(
    private val _repository: ProfileRepository
) {

    operator fun invoke(uid: String, photoUri: Uri) : Flow<Resource<Uri>> {
        return _repository.uploadUserPhoto(uid = uid, photoUri = photoUri)
    }
}