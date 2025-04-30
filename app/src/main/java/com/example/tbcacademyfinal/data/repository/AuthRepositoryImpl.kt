package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.common.safecalls.safeAuthCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Resource<AuthResult>> =
        flow {
            emit(Resource.Loading)
            val res = safeAuthCall {
                auth.signInWithEmailAndPassword(email, password).await()
            }
            emit(res)
        }.flowOn(Dispatchers.IO)

    override fun register(email: String, password: String): Flow<Resource<AuthResult>> =
        flow {
            emit(Resource.Loading)
            val res = safeAuthCall {
                auth.createUserWithEmailAndPassword(email, password).await()
            }
            emit(res)
        }.flowOn(Dispatchers.IO)

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}