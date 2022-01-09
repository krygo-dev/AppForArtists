package com.krygodev.appforartists.feature_image.domain.use_case.image_use_case

import android.net.Uri
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddImage(
    private val _repository: ImageRepository
) {

    operator fun invoke(image: ImageModel, imageUri: Uri?): Flow<Resource<String>> {
        if (imageUri == null) {
            return flow {
                emit(Resource.Error("Musisz wybrać obraz"))
            }
        }
        return _repository.addImage(image = image, imageUri = imageUri)
    }

}