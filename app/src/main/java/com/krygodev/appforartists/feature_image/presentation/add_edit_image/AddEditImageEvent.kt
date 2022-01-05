package com.krygodev.appforartists.feature_image.presentation.add_edit_image

import android.net.Uri

sealed class AddEditImageEvent {
    data class GetImageById(val id: String): AddEditImageEvent()
    data class EnteredDescription(val content: String): AddEditImageEvent()
    data class EnteredTags(val tags: String): AddEditImageEvent()
    data class ChangeImageUri(val uri: Uri?): AddEditImageEvent()
    object GetUserData: AddEditImageEvent()
    object UpdateUserData: AddEditImageEvent()
    object AddImage: AddEditImageEvent()
    object EditImage: AddEditImageEvent()
}