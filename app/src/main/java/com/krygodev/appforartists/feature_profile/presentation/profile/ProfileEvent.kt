package com.krygodev.appforartists.feature_profile.presentation.profile

sealed class ProfileEvent {
    data class GetUserData(val uid: String): ProfileEvent()
    data class AddStars(val count: Int): ProfileEvent()
    object GetUserImages: ProfileEvent()
    object GetUserFavorites: ProfileEvent()
    object UpdateUserData: ProfileEvent()
    object SignOut: ProfileEvent()
}
