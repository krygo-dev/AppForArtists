package com.krygodev.appforartists.feature_profile.data.repository

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel
import com.krygodev.appforartists.feature_profile.domain.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException

@ExperimentalCoroutinesApi
class ChatRepositoryImpl(
    private val _firebaseFirestore: FirebaseFirestore
) : ChatRepository {

    override fun createChatroom(chatroom: ChatroomModel): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        try {
            val ref = _firebaseFirestore.collection(Constants.CHATROOMS_COLLECTION)
                .document()

            chatroom.id = ref.id

            ref.set(chatroom).await()

            emit(Resource.Success(ref.id))

        } catch (e: HttpException) {
            emit(Resource.Error(message = "Coś poszło nie tak!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Nie udało się połączyć z serwerem, sprawdź połączenie z internetem"))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.localizedMessage!!))
        }
    }

    override fun getUserChatrooms(uid: String): Flow<Resource<List<ChatroomModel>>> = callbackFlow {
        val subscription = _firebaseFirestore.collection(Constants.USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    trySend(Resource.Error(message = it.message!!))
                    return@addSnapshotListener
                }
                val result = _firebaseFirestore.collection(Constants.CHATROOMS_COLLECTION)
                    .whereIn("id", snapshot?.toObject(UserModel::class.java)?.chatrooms!!)
                    .get()
                    .result.toObjects(ChatroomModel::class.java)

                trySend(Resource.Success(result))
            }
        awaitClose { subscription.remove() }
    }

    override fun getMessages(chatroom: ChatroomModel): Flow<Resource<List<MessageModel>>> =
        callbackFlow {
            val subscription = _firebaseFirestore.collection(Constants.CHATROOMS_COLLECTION)
                .document(chatroom.id)
                .collection(Constants.MESSAGES_COLLECTION)
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        trySend(Resource.Error(message = it.message!!))
                        return@addSnapshotListener
                    }
                    trySend(Resource.Success(snapshot?.toObjects(MessageModel::class.java)!!))
                }
            awaitClose { subscription.remove() }
        }

    override fun sendMessage(chatroom: ChatroomModel, message: MessageModel): Flow<Resource<Void>> = flow {
        emit(Resource.Loading())

        try {
            val ref = _firebaseFirestore.collection(Constants.CHATROOMS_COLLECTION)
                .document(chatroom.id)
                .collection(Constants.MESSAGES_COLLECTION)
                .document()

            message.id = ref.id

            val result = ref.set(message).await()

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