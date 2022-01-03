package com.krygodev.appforartists.feature_profile.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.domain.util.serializeToMap
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException

class ProfileRepositoryImpl(
    private val _firebaseAuth: FirebaseAuth,
    private val _firebaseFirestore: FirebaseFirestore,
    private val _firebaseStorage: FirebaseStorage
) : ProfileRepository {

    override fun getUserData(uid: String): Flow<Resource<UserModel>> = flow {
        emit(Resource.Loading())

        try {
            val result =
                _firebaseFirestore.collection(Constants.USER_COLLECTION)
                    .document(uid)
                    .get()
                    .await()
                    .toObject(UserModel::class.java)

            emit(Resource.Success(result!!))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun getUserImagesOrFavorites(listOfId: List<String>): Flow<Resource<List<ImageModel>>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .whereIn("id", listOfId)
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

    override fun setOrUpdateUserData(user: UserModel): Flow<Resource<Void>> = flow {
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

    override fun getCurrentUser(): FirebaseUser? {
        return _firebaseAuth.currentUser
    }
}