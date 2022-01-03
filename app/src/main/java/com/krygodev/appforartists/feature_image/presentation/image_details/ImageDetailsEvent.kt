package com.krygodev.appforartists.feature_image.presentation.image_details

import com.krygodev.appforartists.feature_image.domain.model.CommentModel

sealed class ImageDetailsEvent {
    data class GetImageById(val id: String): ImageDetailsEvent()
    data class AddComment(val content: String): ImageDetailsEvent()
    data class EditComment(val comment: CommentModel): ImageDetailsEvent()
    data class DeleteComment(val comment: CommentModel): ImageDetailsEvent()
    data class AddImageToFavorites(val id: String): ImageDetailsEvent()
    data class RemoveFromFavorites(val id: String): ImageDetailsEvent()
    object DeleteImage: ImageDetailsEvent()
}
