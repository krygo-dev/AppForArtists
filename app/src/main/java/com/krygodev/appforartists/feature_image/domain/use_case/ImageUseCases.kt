package com.krygodev.appforartists.feature_image.domain.use_case

data class ImageUseCases(
    val getImageById: GetImageById,
    val addImage: AddImage,
    val deleteImage: DeleteImage
)
