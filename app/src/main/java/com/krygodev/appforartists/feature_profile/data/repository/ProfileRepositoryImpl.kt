package com.krygodev.appforartists.feature_profile.data.repository

import android.net.Uri
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.krygodev.appforartists.core.domain.model.Image
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.domain.util.serializeToMap
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException

class ProfileRepositoryImpl(
    private val _firebaseFirestore: FirebaseFirestore,
    private val _firebaseStorage: FirebaseStorage
) : ProfileRepository {

    override fun getUserData(uid: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())

        try {
            val result =
                _firebaseFirestore.collection(Constants.USER_COLLECTION)
                    .document(uid)
                    .get()
                    .await()
                    .toObject(User::class.java)

            emit(Resource.Success(result!!))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun getUserImages(user: User): Flow<Resource<List<Image>>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .whereIn("uid", user.images!!)
                .get()
                .await()
                .toObjects(Image::class.java)

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun setOrUpdateUserData(user: User): Flow<Resource<Void>> = flow {
        emit(Resource.Loading())

        try {
            val result =
                _firebaseFirestore.collection(Constants.USER_COLLECTION)
                    .document(user.uid!!)
                    .update(user.serializeToMap())
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

    override fun uploadUserPhoto(uid: String, photoUri: Uri): Flow<Resource<Uri>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseStorage.getReference(Constants.USER_PHOTOS_BUCKET)
                .child("$uid.jpg")
                .putFile(photoUri)
                .await()
                .storage.downloadUrl.await()

            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Something went wrong!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection!"))
        } catch (e: StorageException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }
}