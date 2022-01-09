package com.krygodev.appforartists.feature_image.domain.use_case.home_use_case

data class HomeUseCases(
    val getRandomImage: GetRandomImage,
    val getMostLikedImages: GetMostLikedImages,
    val getBestRatedImages: GetBestRatedImages,
    val getRecentlyAddedImages: GetRecentlyAddedImages,
    val getBestRatedUsers: GetBestRatedUsers
)
