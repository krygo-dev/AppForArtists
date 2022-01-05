package com.krygodev.appforartists.feature_image.presentation.add_edit_image

sealed class AddEditImageEvent {
    data class GetImageById(val id: String): AddEditImageEvent()
    data class EnteredDescription(val content: String): AddEditImageEvent()
    data class EnteredTags(val tags: String): AddEditImageEvent()
    object GetUserData: AddEditImageEvent()
    object UpdateUserData: AddEditImageEvent()
    object AddImage: AddEditImageEvent()
    object EditImage: AddEditImageEvent()
}