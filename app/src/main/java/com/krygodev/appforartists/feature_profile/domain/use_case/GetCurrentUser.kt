package com.krygodev.appforartists.feature_profile.domain.use_case

import com.google.firebase.auth.FirebaseUser
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository

class GetCurrentUser(
    private val _repository: ProfileRepository
) {

    operator fun invoke(): FirebaseUser? {
        return _repository.getCurrentUser()
    }

}