package com.krygodev.appforartists.feature_profile.presentation.profile

sealed class ProfileEvent {
    data class GetUserData(val uid: String): ProfileEvent()
    object GetUserImages: ProfileEvent()
    object GetUserFavorites: ProfileEvent()
    object SignOut: ProfileEvent()
}
