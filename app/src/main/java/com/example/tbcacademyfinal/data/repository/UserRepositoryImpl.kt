package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.domain.model.User
import com.example.tbcacademyfinal.domain.repository.UserRepository
import com.example.tbcacademyfinal.common.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    override suspend fun createUserProfile(user: User): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            firestore.collection(USERS_COLLECTION)
                .document(user.uid) // Use Firebase Auth UID as document ID
                .set(user) // Store the User data class (Firestore handles serialization)
                .await()
            emit(Resource.Success(Unit)) // Success means void operation completed
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Failed to create user profile in Firestore.",
                    e
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getUserProfile(uid: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val documentSnapshot = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()

            val user =
                documentSnapshot.toObject(User::class.java) // Deserialize back to User data class
            if (user != null) {
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("User profile not found in Firestore."))
            }
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Failed to fetch user profile from Firestore.", e
                )
            )
        }
    }.flowOn(Dispatchers.IO)

}