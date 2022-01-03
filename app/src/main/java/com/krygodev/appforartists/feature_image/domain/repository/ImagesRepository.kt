package com.krygodev.appforartists.feature_image.domain.repository

import android.net.Uri
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    fun getImageById(imageId: String) : Flow<Resource<ImageModel>>

    fun addImage(image: ImageModel, imageUri: Uri) : Flow<Resource<Void>>

    fun deleteImage(image: ImageModel) : Flow<Resource<Void>>
}