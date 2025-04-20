package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    // Example of listening to auth state:
    // override val authState: Flow<FirebaseUser?> = callbackFlow {
    //     val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
    //         trySend(firebaseAuth.currentUser).isSuccess
    //     }
    //     auth.addAuthStateListener(listener)
    //     awaitClose { auth.removeAuthStateListener(listener) }
    // }.flowOn(Dispatchers.IO)

    override suspend fun login(email: String, password: String): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading)
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error("Invalid email or password."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred during login.", e))
        }
    }.flowOn(Dispatchers.IO) // Perform network/auth operations on IO dispatcher

    override suspend fun register(email: String, password: String): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading)
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        } catch (e: FirebaseAuthWeakPasswordException) {
            emit(Resource.Error("Password is too weak. Please choose a stronger password."))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Although less likely for registration, handle it just in case (e.g., malformed email)
            emit(Resource.Error("Invalid email format."))
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Resource.Error("An account with this email already exists."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred during registration.", e))
        }
    }.flowOn(Dispatchers.IO) // Perform network/auth operations on IO dispatcher

    override suspend fun logout() {
        auth.signOut()
        // No return needed, sign out is fire-and-forget
    }
}