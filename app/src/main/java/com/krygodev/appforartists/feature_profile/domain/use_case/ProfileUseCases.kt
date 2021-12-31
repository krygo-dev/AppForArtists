package com.krygodev.appforartists.feature_profile.domain.use_case

data class ProfileUseCases(
    val getUserData: GetUserData,
    val getUserImages: GetUserImages,
    val getUserFavorites: GetUserFavorites,
    val setOrUpdateUserData: SetOrUpdateUserData,
    val uploadUserPhoto: UploadUserPhoto,
    val getCurrentUser: GetCurrentUser
)
