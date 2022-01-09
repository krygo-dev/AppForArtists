package com.krygodev.appforartists.feature_image.domain.repository

import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getRandomImage() : Flow<Resource<ImageModel>>

    fun getMostLikedImages(limit: Int) : Flow<Resource<List<ImageModel>>>

    fun getBestRatedImages(limit: Int) : Flow<Resource<List<ImageModel>>>

    fun getRecentlyAddedImages(limit: Int) : Flow<Resource<List<ImageModel>>>
}