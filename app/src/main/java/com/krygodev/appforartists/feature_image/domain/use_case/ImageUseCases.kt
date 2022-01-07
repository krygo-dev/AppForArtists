package com.krygodev.appforartists.feature_image.domain.use_case

data class ImageUseCases(
    val getImageById: GetImageById,
    val addImage: AddImage,
    val editImage: EditImage,
    val deleteImage: DeleteImage,
    val getImageComments: GetImageComments,
    val addComment: AddComment,
    val deleteComment: DeleteComment
)
