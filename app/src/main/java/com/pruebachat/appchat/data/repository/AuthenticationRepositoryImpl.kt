package com.pruebachat.appchat.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.pruebachat.appchat.core.Response
import com.pruebachat.appchat.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementación de [AuthenticationRepository] que utiliza Firebase Authentication para la autenticación de usuarios.
 *
 * @param auth Instancia de FirebaseAuth proporcionada por inyección de dependencias.
 */
class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthenticationRepository {


    /**
     * Método  que obtiene el UID del usuario actualmente autenticado.
     *
     * @return String que representa el UID del usuario actual, o una cadena vacía si no hay usuario autenticado.
     */
    override suspend fun userUid(): String = auth.currentUser?.uid ?: ""



    /**
     * Método para cerrar sesión del usuario actual.
     */


    override suspend fun logout() = auth.signOut()


    /**
     * Método para iniciar sesión con el email y password proporcionados.
     *
     * @param email Email del usuario para iniciar sesión.
     * @param password Password del usuario para iniciar sesión.
     * @return Flow que emite un objeto Response indicando el resultado de la operación de inicio de sesión.
     */

    override suspend fun login(email: String, password: String): Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)
            val data = auth.signInWithEmailAndPassword(email, password).await()
            emit(Response.Success(data))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error, Intente nuevamente."))
        }
    }

    /**
     * Método Registra un nuevo usuario con el email y password proporcionados.
     *
     * @param email Email del usuario para registrar.
     * @param password Password del usuario para registrar.
     * @return Flow que emite un objeto Response indicando el resultado de la operación de registro.
     */

    override suspend fun registro(email: String, password: String): Flow<Response<AuthResult>> =
        flow {
            try {
                emit(Response.Loading)
                val data = auth.createUserWithEmailAndPassword(email, password).await()
                emit(Response.Success(data))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage ?: "Error, intente nuevamente."))
            }
        }


}