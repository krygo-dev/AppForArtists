package com.krygodev.appforartists.feature_profile.presentation.edit_profile

import android.net.Uri

sealed class EditProfileEvent {
    object GetUserData: EditProfileEvent()
    object UpdateUserData: EditProfileEvent()
    data class UpdateBio(val value: String): EditProfileEvent()
    data class UpdatePhoto(val value: Uri): EditProfileEvent()
}