package com.krygodev.appforartists.feature_profile.presentation.profile

sealed class ProfileEvent {
    object GetUserData: ProfileEvent()
    object GetUserImages: ProfileEvent()
    object GetUserFavorites: ProfileEvent()
    object SignOut: ProfileEvent()
}
