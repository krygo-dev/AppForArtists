package com.krygodev.appforartists.feature_image.data.repository

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import kotlin.random.Random
import kotlin.random.nextInt

class HomeRepositoryImpl(
    private val _firebaseFirestore: FirebaseFirestore
) : HomeRepository {

    override fun getRandomImage(): Flow<Resource<ImageModel>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .get()
                .await()
                .toObjects(ImageModel::class.java)

            if (result.size > 0) {
                val index = Random.nextInt(0 until result.size)

                emit(Resource.Success(result[index]!!))
            }

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun getMostLikedImages(limit: Int): Flow<Resource<List<ImageModel>>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .orderBy("likes", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .toObjects(ImageModel::class.java)

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun getBestRatedImages(limit: Int): Flow<Resource<List<ImageModel>>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .orderBy("starsAvg", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .toObjects(ImageModel::class.java)

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun getRecentlyAddedImages(limit: Int): Flow<Resource<List<ImageModel>>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .toObjects(ImageModel::class.java)

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun getBestRatedUsers(limit: Int): Flow<Resource<List<UserModel>>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.USER_COLLECTION)
                .orderBy("starsAvg", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .toObjects(UserModel::class.java)

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }
}