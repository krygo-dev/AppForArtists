package com.krygodev.appforartists.feature_image.presentation.image_details

import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.feature_image.domain.model.CommentModel

sealed class ImageDetailsEvent {
    data class GetImageById(val id: String): ImageDetailsEvent()
    data class GetImageComments(val id: String) :ImageDetailsEvent()
    data class DeleteComment(val comment: CommentModel): ImageDetailsEvent()
    data class AddImageToFavorites(val id: String): ImageDetailsEvent()
    data class RemoveFromFavorites(val id: String): ImageDetailsEvent()
    data class UpdateUserData(val user: UserModel): ImageDetailsEvent()
    data class EnteredCommentContent(val content: String): ImageDetailsEvent()
    object LikeImage: ImageDetailsEvent()
    object DislikeImage: ImageDetailsEvent()
    object AddComment: ImageDetailsEvent()
    object DeleteImage: ImageDetailsEvent()
    object GetUserData: ImageDetailsEvent()
    object UpdateImageLikes: ImageDetailsEvent()
}
