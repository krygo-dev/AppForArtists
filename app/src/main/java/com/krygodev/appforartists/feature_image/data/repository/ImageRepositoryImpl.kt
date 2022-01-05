package com.krygodev.appforartists.feature_image.data.repository

import android.net.Uri
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.domain.util.serializeToMap
import com.krygodev.appforartists.feature_image.domain.model.CommentModel
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException

class ImageRepositoryImpl(
    private val _firebaseFirestore: FirebaseFirestore,
    private val _firebaseStorage: FirebaseStorage
) : ImageRepository {

    override fun getImageById(id: String): Flow<Resource<ImageModel>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .document(id)
                .get()
                .await()
                .toObject(ImageModel::class.java)

            emit(Resource.Success(result!!))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun addImage(image: ImageModel, imageUri: Uri): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        try {
            val ref = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION).document()

            image.id = ref.id
            image.url = uploadImageToStorage(imageId = image.id!!, imageUri = imageUri)

            ref.set(image).await()

            emit(Resource.Success(image.id!!))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun editImage(image: ImageModel): Flow<Resource<Void>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .document(image.id!!)
                .update(image.serializeToMap())
                .await()

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun deleteImage(image: ImageModel): Flow<Resource<Void>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .document(image.id!!)
                .delete()
                .await()
                .also {
                    deleteImageFromStorage(imageId = image.id!!)
                }

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun getImageComments(id: String): Flow<Resource<List<CommentModel>>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .document(id)
                .collection(Constants.COMMENTS_COLLECTION)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(CommentModel::class.java)

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun addOrEditComment(comment: CommentModel, id: String): Flow<Resource<Void>> = flow {
        emit(Resource.Loading())

        try {
            if (comment.id.isNullOrEmpty()) {
                val ref = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                    .document(id)
                    .collection(Constants.COMMENTS_COLLECTION)
                    .document()

                comment.id = ref.id
            }

            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .document(id)
                .collection(Constants.COMMENTS_COLLECTION)
                .document(comment.id!!)
                .set(comment)
                .await()

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun deleteComment(comment: CommentModel, id: String): Flow<Resource<Void>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .document(id)
                .collection(Constants.COMMENTS_COLLECTION)
                .document(comment.id!!)
                .delete()
                .await()

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    private suspend fun uploadImageToStorage(imageId: String, imageUri: Uri): String {
        return _firebaseStorage.getReference(Constants.IMAGES_BUCKET)
            .child("$imageId.jpg")
            .putFile(imageUri)
            .await()
            .storage.downloadUrl.await().toString()
    }

    private suspend fun deleteImageFromStorage(imageId: String) {
        _firebaseStorage.getReference(Constants.IMAGES_BUCKET)
            .child("$imageId.jpg")
            .delete()
            .await()
    }
}