package com.krygodev.appforartists.feature_profile.data.repository

import android.net.Uri
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.krygodev.appforartists.core.domain.model.Painting
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
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

    override fun getUserPaintings(user: User): Flow<Resource<List<Painting>>> = flow {
        emit(Resource.Loading())

        try {
            val result = _firebaseFirestore.collection(Constants.IMAGES_COLLECTION)
                .whereIn("uid", user.paintings!!)
                .get()
                .await()
                .toObjects(Painting::class.java)

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

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun uploadUserPhoto(uid: String, photoUri: Uri): Flow<Resource<Uri>> {
        TODO("Not yet implemented")
    }
}